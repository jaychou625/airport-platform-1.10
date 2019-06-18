package com.br.service.task;

import com.alibaba.fastjson.JSON;
import com.br.airporttaskserver.service.TrafficTaskStateService;
import com.br.constant.MQConstant;
import com.br.constant.RedisDataConstant;
import com.br.constant.WSMessageConstant;
import com.br.entity.map.CarInfo;
import com.br.entity.task.AewInfo;
import com.br.entity.task.TaskStateInfo;
import com.br.entity.task.WorkCondition;
import com.br.entity.websocket.WSMessage;
import com.br.service.aew.AewService;
import com.br.service.map.MapService;
import com.br.service.redis.RedisService;
import com.br.service.traffic.PositionService;
import com.br.service.websocket.WSService;
import com.br.utils.DateUtils;
import com.route.broadcast.ConflictPoint;
import com.route.broadcast.PositionNotice;
import com.route.imp.NavPoint;
import com.route.imp.PositionPoint;
import com.route.imp.nav.Distance;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Vector;


/**
 * 交通工具任务服务
 *
 * @Author Zero
 * @Date 2019 03 21
 */

@Service("trafficTaskService")
public class TrafficTaskService {

    // 地图服务
    @Autowired
    private MapService mapService;

    // Redis服务
    @Autowired
    private RedisService redisService;

    // WebSocket 服务
    @Autowired
    private WSService wsService;

    // 位置服务
    @Autowired
    private PositionService positionService;

    // 预警信息 Mapper
    @Autowired
    private AewService aewService;

    // 任务状态服务
    @Autowired
    private TrafficTaskStateService trafficTaskStateService;

    // 时间工具类
    @Autowired
    private DateUtils dateUtils;

    // RabbitTemplate
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 处理任务
     *
     * @param carInfo 车辆信息
     */
    public void handleTask(CarInfo carInfo, boolean isReRoute) {
        /*--------------获取任务状态------------------*/
        TaskStateInfo taskStateInfo = this.trafficTaskStateService.getTaskState(carInfo, isReRoute,"");
        /*--------------构建位置坐标------------------*/
        PositionPoint positionPoint = this.buildPositionPoint(carInfo);
        /*--------------存入位置------------------*/
        this.positionService.saveCarInfo(positionPoint);
        /*--------------创建导航坐标------------------*/
        NavPoint navPoint = this.buildNavPoint(positionPoint, 1, taskStateInfo.getPortNo(), null);
        /*--------------判断任务状态执行业务------------------*/
        switch (taskStateInfo.getState()) {
            case 0:
                /*--------------路径规划------------------*/
                this.eventRoutePlan(navPoint);
                break;
            case 1:
                /*--------------计算工况------------------*/
                this.calcWorkCondition(carInfo, positionPoint, taskStateInfo);
                /*--------------计算非法区域------------------*/
                this.calcEntryArea(carInfo, taskStateInfo);
                /*--------------计算车辆过近------------------*/
                this.calcNearCarConflict(taskStateInfo);
                /*--------------计算冲突点------------------*/
                this.calcCrossConflict(taskStateInfo);
                break;
            case 2:
                /*--------------任务结束------------------*/
                this.eventClean(positionPoint.deviceNo);
                break;
            default:
                break;
        }
    }

    /**
     * 计算任务工况
     *
     * @param positionPoint 位置坐标
     */
    public void calcWorkCondition(CarInfo carInfo, PositionPoint positionPoint, TaskStateInfo taskStateInfo) {
        /*--------------从Redis获取位置坐标的路径规划------------------*/
        Distance currentRoute = (Distance) this.redisService.getCacheOfHash(RedisDataConstant.HASH_ROUTES, positionPoint.deviceNo);
        /*--------------判断是否为空------------------*/
        if (!ObjectUtils.isEmpty(currentRoute)) {
            /*--------------计算工况------------------*/
            Vector<PositionNotice> positionNotices = this.mapService.calcWorkCondition(positionPoint, currentRoute);
            /*--------------判断是否为空------------------*/
            if (!CollectionUtils.isEmpty(positionNotices)) {
                /*--------------遍历工况------------------*/
                for (PositionNotice notice : positionNotices) {
                    /*--------------添加工况预警信息------------------*/
                    this.aewService.add(this.buildAewInfo(taskStateInfo, notice));
                    /*--------------推送工况到预发送处------------------*/
                    WorkCondition workCondition = new WorkCondition(carInfo, notice);
                    /*--------------推送工况到预发送处------------------*/
                    this.rabbitTemplate.convertAndSend(MQConstant.EXCHANGE_TOPIC, MQConstant.TOPIC_WORK_CONDITION, workCondition);
                    /*--------------偏离路径 路径重规划------------------*/
                    if (notice.noticeType == PositionNotice.TYPE_FARAWAY) {
                        /*--------------清除事件------------------*/
                        this.eventClean(positionPoint.deviceNo);
                        /*--------------得到此车辆设备坐标------------------*/
                        positionPoint = (PositionPoint) this.redisService.getCacheOfHash(RedisDataConstant.HASH_CAR, positionPoint.deviceNo);
                        /*--------------构建新的导航坐标------------------*/
                        carInfo.setCarLongitude(new BigDecimal(positionPoint.gpsPosition.iLongititude));
                        carInfo.setCarLatitude(new BigDecimal(positionPoint.gpsPosition.iLatitude));
                        /*--------------重规划路径------------------*/
                        this.handleTask(carInfo, true);
                    }
                    /*--------------任务强制结束------------------*/
                    if (notice.noticeType == PositionNotice.TYPE_END) {
                        /*--------------清除事件------------------*/
                        this.eventClean(positionPoint.deviceNo);
                    }
                }
            }
        }
    }

    /**
     * 计算任务过近车辆冲突
     */
    public void calcNearCarConflict(TaskStateInfo taskStateInfo) {
        /*--------------获取全部规划和车辆位置数组------------------*/
        Collection[] collections = getInTaskPositionsAndRoutes();
        /*--------------计算过近车辆预警------------------*/
        Vector<PositionNotice> nearCarConflicts = this.mapService.calcNearCarConflict((Vector) collections[0], (Vector) collections[1]);
        /*--------------判断是否为空------------------*/
        if (!CollectionUtils.isEmpty(nearCarConflicts)) {
            /*--------------遍历预警存入数据库------------------*/
            for (PositionNotice notice : nearCarConflicts) {
                this.aewService.add(this.buildAewInfo(taskStateInfo, notice));
            }
        }
    }

    /**
     * 计算非法区域
     *
     * @param taskStateInfo 任务状态信息
     */
    public void calcEntryArea(CarInfo carInfo, TaskStateInfo taskStateInfo) {
        /*--------------获取当前车辆的位置坐标------------------*/
        PositionPoint positionPoint = this.buildPositionPoint(carInfo);
        /*--------------获取当前规划------------------*/
        Distance distance = this.getRoutePlan(taskStateInfo.getCarNo());
        /*--------------判断是否为空------------------*/
        if (!ObjectUtils.isEmpty(positionPoint) && !ObjectUtils.isEmpty(distance)) {
            /*--------------计算非法区域------------------*/
            Vector<PositionNotice> entryAreas = this.mapService.calcEntryArea(positionPoint, distance);
            /*--------------遍历预警存入数据库------------------*/
            for (PositionNotice notice : entryAreas) {
                this.aewService.add(this.buildAewInfo(taskStateInfo, notice));
            }
        }
    }

    /**
     * 计算冲突点
     *
     * @param taskStateInfo 任务状态信息
     */
    public void calcCrossConflict(TaskStateInfo taskStateInfo) {
        /*--------------获取全部规划和车辆位置数组------------------*/
        Collection[] collections = getInTaskPositionsAndRoutes();
        /*--------------计算过近车辆预警------------------*/
        Vector<ConflictPoint> crossConflicts = this.mapService.calcCrossConflict((Vector) collections[0], (Vector) collections[1]);
        /*--------------判断是否为空------------------*/
        if (!CollectionUtils.isEmpty(crossConflicts)) {
            /*--------------遍历预警存入数据库------------------*/
            for (ConflictPoint conflictPoint : crossConflicts) {
                this.aewService.add(this.buildAewInfo(taskStateInfo, conflictPoint));
                /*--------------发送到前端------------------*/
                this.eventCrossConflict(taskStateInfo, conflictPoint);
            }
        }
    }


    /**
     * 获取在任务中的交通设备位置与路径
     *
     * @return Collection[]
     */
    public Collection[] getInTaskPositionsAndRoutes() {
        /*--------------在任务中的车辆信息集合------------------*/
        Vector<PositionPoint> inTaskCarPositions = new Vector();
        /*--------------获取全部车辆位置坐标------------------*/
        Vector<PositionPoint> carPositions = this.positionService.getAllCarsInfo();
        /*--------------获取有规划的车辆------------------*/
        Vector<Distance> routes = this.mapService.getAllRoutes();
        /*--------------判断是否为空------------------*/
        if (!CollectionUtils.isEmpty(routes) && !CollectionUtils.isEmpty(carPositions)) {
            /*--------------遍历比较------------------*/
            for (Distance route : routes) {
                for (PositionPoint positionPoint : carPositions) {
                    if (positionPoint.deviceNo.equals(route.deviceNo)) {
                        inTaskCarPositions.add(positionPoint);
                    }
                }
            }
            /*--------------判断是否为空------------------*/
            if (!CollectionUtils.isEmpty(inTaskCarPositions) && inTaskCarPositions.size() == routes.size()) {
                /*--------------集合排序------------------*/
                inTaskCarPositions.sort((PositionPoint p1, PositionPoint p2) -> p1.deviceNo.compareToIgnoreCase(p2.deviceNo));
                routes.sort((Distance d1, Distance d2) -> d1.deviceNo.compareToIgnoreCase(d2.deviceNo));
            }
        }
        /*--------------返回集合数组------------------*/
        return new Collection[]{inTaskCarPositions, routes};
    }

    /**
     * 任务路径规划事件
     *
     * @param navPoint 导航坐标
     */
    public void eventRoutePlan(NavPoint navPoint) {
        /*--------------路径规划------------------*/
        Distance route = this.mapService.routePlan(navPoint);
        /*--------------存储此任务路径规划------------------*/
        this.redisService.saveCacheOfHash(RedisDataConstant.HASH_ROUTES, route.deviceNo, route);
        /*--------------发送到前端------------------*/
        this.wsService.wsSend(new WSMessage(WSMessageConstant.EVENT_ROUTE_PLAN, JSON.toJSONString(route)));
    }

    /**
     * 冲突点事件
     *
     * @param taskStateInfo
     * @param conflictPoint
     */
    public void eventCrossConflict(TaskStateInfo taskStateInfo, ConflictPoint conflictPoint) {
        /*--------------存储此任务路径规划------------------*/
        this.redisService.saveCacheOfHash(RedisDataConstant.HASH_CROSS, taskStateInfo.getCarNo(), conflictPoint);
        /*--------------发送到前端------------------*/
        this.wsService.wsSend(new WSMessage(WSMessageConstant.EVENT_CROSS_POINT, JSON.toJSONString(conflictPoint)));
    }

    /**
     * 数据清除事件
     *
     * @param deviceNo 交通工具设备号
     */
    public void eventClean(String deviceNo) {
        /*--------------发送到前端------------------*/
        this.wsService.wsSend(new WSMessage(WSMessageConstant.EVENT_CLEAN, deviceNo));
        /*--------------清楚Redis中相关数据------------------*/
        this.redisService.removeCacheOfHash(RedisDataConstant.HASH_CROSS, deviceNo);
        this.redisService.removeCacheOfHash(RedisDataConstant.HASH_ROUTES, deviceNo);
    }

    /**
     * 构建预警信息
     *
     * @param taskStateInfo 任务状态信息
     * @param notice        预警信息
     * @return AewInfo
     */
    public AewInfo buildAewInfo(TaskStateInfo taskStateInfo, PositionNotice notice) {
        AewInfo aewInfo = new AewInfo();
        aewInfo.setCarSeq(taskStateInfo.getCarSeq());
        aewInfo.setAewSeq(notice.noticeType);
        aewInfo.setPlaneSeq(taskStateInfo.getFltNo());
        aewInfo.setDriverName(taskStateInfo.getDriverName());
        aewInfo.setAewInfoTime(this.dateUtils.longToDate(notice.calTime));
        return aewInfo;
    }

    /**
     * 获取路径规划
     *
     * @param trafficSeq 交通工具序号
     * @return Distance
     */
    public Distance getRoutePlan(String trafficSeq) {
        return (Distance) this.redisService.getCacheOfHash(RedisDataConstant.HASH_ROUTES, trafficSeq);
    }

    /**
     * 构建导航坐标
     *
     * @param positionPoint 位置坐标
     * @param endPointType  终点坐标类型
     * @param portNo        机位
     * @param endPoint      终点坐标
     * @return
     */
    public NavPoint buildNavPoint(PositionPoint positionPoint, Integer endPointType, String portNo,
                                  double[] endPoint) {
        NavPoint navPoint = new NavPoint();
        navPoint.zoneID = "4602000001";
        navPoint.deviceNo = positionPoint.deviceNo;
        navPoint.deviceType = positionPoint.deviceType;
        navPoint.startPoint.iLongititude = positionPoint.gpsPosition.iLongititude;
        navPoint.startPoint.iLatitude = positionPoint.gpsPosition.iLatitude;
        navPoint.endPointType = endPointType;
        if (endPointType == 1) {
            navPoint.endPointName = portNo;
        } else if (endPointType == 2) {
            navPoint.endPoint.iLongititude = endPoint[0];
            navPoint.endPoint.iLatitude = endPoint[1];
        }
        return navPoint;
    }

    /**
     * 构建位置坐标
     *
     * @param carInfo 车辆信息
     * @return PositionPoint 位置坐标
     */
    public PositionPoint buildPositionPoint(CarInfo carInfo) {
        PositionPoint positionPoint = new PositionPoint();
        positionPoint.deviceNo = carInfo.getCar().getCarNo();
        positionPoint.carType = getCarType(carInfo.getCar().getCarType());
        positionPoint.deviceType = "car";
        positionPoint.gpsPosition.iLongititude = carInfo.getCarLongitude().doubleValue();
        positionPoint.gpsPosition.iLatitude = carInfo.getCarLatitude().doubleValue();
        return positionPoint;
    }


    /**
     * 获取车辆类型
     *
     * @param carType 汽车类型文本
     * @return Integer
     */
    public Integer getCarType(String carType) {
        switch (carType) {
            case "客梯车":
                return 1;
            case "引导车":
                return 2;
            case "牵引车":
                return 3;
            case "摆渡车":
                return 4;
            default:
                return 0;
        }
    }
}
