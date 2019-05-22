package com.br.service.service.task;

import com.alibaba.fastjson.JSON;
import com.br.entity.map.CarInfo;
import com.br.entity.task.AewInfo;
import com.br.entity.task.TaskStateInfo;
import com.br.entity.task.TrafficTask;
import com.br.entity.websocket.WSMessage;
import com.br.service.constant.RedisDataConstant;
import com.br.service.constant.WSMessageConstant;
import com.br.service.service.map.MapService;
import com.br.service.service.redis.RedisService;
import com.br.service.service.traffic.AewService;
import com.br.service.service.traffic.PositionService;
import com.br.service.service.websocket.WSService;
import com.route.broadcast.PositionNotice;
import com.route.imp.NavPoint;
import com.route.imp.PositionPoint;
import com.route.imp.nav.Distance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
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


    /**
     * 处理任务
     *
     * @param carInfo 车辆信息
     */
    public void handleTask(CarInfo carInfo) {
        /*--------------获取任务状态------------------*/
        TaskStateInfo taskStateInfo = getTaskState(carInfo);
        /*--------------构建位置坐标------------------*/
        PositionPoint positionPoint = this.buildPositionPoint(carInfo);
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
                this.calcWorkCondition(carInfo, positionPoint);
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
     * 数据清除事件
     *
     * @param deviceNo 交通工具设备号
     */
    public void eventClean(String deviceNo) {
        this.wsService.wsSend(new WSMessage(WSMessageConstant.EVENT_CLEAN, deviceNo));
    }


    /**
     * 计算工况
     *
     * @param positionPoint 位置坐标
     */
    public void calcWorkCondition(CarInfo carInfo, PositionPoint positionPoint) {
        /*--------------从Redis获取位置坐标的路径规划------------------*/
        Distance currentRoute = (Distance) this.redisService.getCacheOfHash(RedisDataConstant.HASH_ROUTES, positionPoint.deviceNo);
        /*--------------计算工况------------------*/
        Vector<PositionNotice> positionNotices = this.mapService.calcWorkCondition(positionPoint, currentRoute);
        /*--------------遍历工况------------------*/
        for (PositionNotice notice : positionNotices) {
            /*--------------添加工况预警信息------------------*/
            this.aewService.add(new AewInfo());
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
                this.handleTask(carInfo);
            }
            /*--------------任务强制结束------------------*/
            if (notice.noticeType == PositionNotice.TYPE_END) {
                /*--------------清除事件------------------*/
                this.eventClean(positionPoint.deviceNo);
            }
        }
    }


    /**
     * 记录任务
     *
     * @param trafficTask 交通工具任务实例
     */

/*    public void saveTrafficTask(TrafficTask trafficTask, boolean isReRoute) {
        if (!hasTask(trafficTask.getCarSeq()) && !isReRoute) {
            this.redisService.saveCacheOfHash(RedisDataConstant.HASH_TASK, trafficTask.getCarSeq(), trafficTask);
        }
        if (hasTask(trafficTask.getCarSeq()) && isReRoute) {
            this.redisService.saveCacheOfHash(RedisDataConstant.HASH_TASK, trafficTask.getCarSeq(), trafficTask);
        }
        PositionPoint positionPoint = this.getTrafficInfo(trafficTask.getCarSeq());
        switch (positionPoint.carType) {
            case PositionPoint.CAR_TYPE_GUIDEDCAR:
                this.carTaskHandler(trafficTask, positionPoint, isReRoute);
                break;
            default:
                System.out.println("...");
        }
    }*/

    /*    *//**
     * 车辆路径规划
     *
     * @param trafficInfo 交通工具信息
     *//*
    public void carTaskHandler(TrafficTask trafficTask, PositionPoint trafficInfo, boolean isReRoute) {
        NavPoint navPoint;
        Distance distance = null;
        switch (trafficTask.getTaskEvent()) {
            case "start_task":
                if (isReRoute) {
                    this.redisService.removeCacheOfHash(RedisDataConstant.HASH_ROUTES, trafficInfo.deviceNo);
                    this.wsService.wsSend(new WSMessage(WSMessageConstant.EVENT_RE_ROUTE_PLAN, JSON.toJSONString(trafficTask.getCarSeq())));
                }
                navPoint = buildNavPoint(trafficInfo, trafficTask, 1);
                distance = this.mapService.routePlan(navPoint);
                this.redisService.saveCacheOfHash(RedisDataConstant.HASH_ROUTES, trafficInfo.deviceNo, distance);
                this.wsService.wsSend(new WSMessage(WSMessageConstant.EVENT_ROUTE_PLAN, JSON.toJSONString(distance)));
                break;
            case "finish_task":
                distance = this.getRoutePlan(trafficTask.getCarSeq());
                this.redisService.removeCacheOfHash(RedisDataConstant.HASH_ROUTES, trafficInfo.deviceNo);
                this.redisService.removeCacheOfHash(RedisDataConstant.HASH_TASK, trafficInfo.deviceNo);
                this.redisService.removeCacheOfHash(RedisDataConstant.HASH_AEW, "condition-" + trafficInfo.deviceNo);
                this.redisService.removeCacheOfHash(RedisDataConstant.HASH_AEW, "entry-" + trafficInfo.deviceNo);
                this.redisService.removeCacheOfHash(RedisDataConstant.HASH_AEW, "cross-" + trafficInfo.deviceNo);
                this.redisService.removeCacheOfHash(RedisDataConstant.HASH_AEW, "near-" + trafficInfo.deviceNo);
                this.wsService.wsSend(new WSMessage(WSMessageConstant.EVENT_ARRIVE_TARGET, JSON.toJSONString(trafficTask.getCarSeq())));
                break;
            default:
                System.out.println("...");
        }
    }*/

    /* *
     * 当前交通工具的预警计算
     *
     * @param trafficInfo
     */
    /*public void trafficAewInfoHandler(PositionPoint trafficInfo) {
        if (this.hasTask(trafficInfo.deviceNo) && this.hasRoute(trafficInfo.deviceNo)) {
            TrafficTask trafficTask = this.getTrafficTask(trafficInfo.deviceNo);
            Distance trafficRoute = this.getRoutePlan(trafficInfo.deviceNo);
            Vector<PositionNotice> workConditions = this.mapService.calcWorkCondition(trafficInfo, trafficRoute);
            if (workConditions != null && workConditions.size() > 0) {
                for (PositionNotice positionNotice : workConditions) {
                    if (positionNotice.noticeType == PositionNotice.TYPE_FARAWAY) {
                        this.saveTrafficTask(trafficTask, true);
                    } else {
                        this.aewService.add(this.buildAewInfo(positionNotice, trafficTask));
                        this.redisService.saveCacheOfHash(RedisDataConstant.HASH_AEW, "condition-" + trafficInfo.deviceNo, positionNotice);
                    }
                }
            }
            Vector<PositionNotice> entryAreas = this.mapService.calcEntryArea(trafficInfo, trafficRoute);
            if (entryAreas != null && entryAreas.size() > 0) {
                for (PositionNotice positionNotice : entryAreas) {
                    this.aewService.add(this.buildAewInfo(positionNotice, trafficTask));
                    this.redisService.saveCacheOfHash(RedisDataConstant.HASH_AEW, "entry-" + trafficInfo.deviceNo, positionNotice);
                }
            }
            Vector<PositionPoint> trafficInfos = this.positionService.getAllTrafficInfos();
            Vector<Distance> routes = this.mapService.getAllRoutes();
            if (trafficInfos.size() == routes.size() && trafficInfos != null && routes != null) {
                Vector<ConflictPoint> crossConflicts = this.mapService.calcCrossConflict(trafficInfos, routes);
                if (crossConflicts != null && crossConflicts.size() > 0) {
                    for (ConflictPoint conflictPoint : crossConflicts) {
                        this.aewService.add(this.buildAewInfo(conflictPoint, trafficTask));
                        this.redisService.saveCacheOfHash(RedisDataConstant.HASH_AEW, "cross-" + trafficInfo.deviceNo, conflictPoint);
                        this.wsService.wsSend(new WSMessage(WSMessageConstant.EVENT_ROUTE_CROSS, JSON.toJSONString(conflictPoint)));
                    }
                }
                Vector<PositionNotice> nearCarConflicts = this.mapService.calcNearCarConflict(trafficInfos, routes);
                if (nearCarConflicts != null && nearCarConflicts.size() > 0) {
                    for (PositionNotice positionNotice : nearCarConflicts) {
                        this.aewService.add(this.buildAewInfo(positionNotice, trafficTask));
                        this.redisService.saveCacheOfHash(RedisDataConstant.HASH_AEW, "near-" + trafficInfo.deviceNo, positionNotice);
                    }
                }
            }
        }
    }*/


    /**
     * 构建预警信息
     *
     * @param positionNotice 预警结果实例
     * @param trafficTask    交通工具任务
     * @return AewInfo
     */
    public AewInfo buildAewInfo(PositionNotice positionNotice, TrafficTask trafficTask) {
        AewInfo aewInfo = new AewInfo();
        aewInfo.setCarSeq(Integer.parseInt(trafficTask.getCarSeq()));
        aewInfo.setAewSeq(positionNotice.noticeType);
        aewInfo.setPlaneSeq(trafficTask.getPlaneSeq());
        aewInfo.setDriverSeq(1);
        aewInfo.setAewInfoTime(new Date(positionNotice.calTime));
        return aewInfo;
    }

    /**
     * 判断是否存在此交通工具的任务
     *
     * @param carSeq
     * @return
     */
    public boolean hasTask(String carSeq) {
        return this.redisService.hasCacheOfHash(RedisDataConstant.HASH_TASK, carSeq);
    }

    /**
     * 判断是否存在此交通工具的路径规划
     *
     * @param trafficSeq
     * @return
     */
    public boolean hasRoute(String trafficSeq) {
        return this.redisService.hasCacheOfHash(RedisDataConstant.HASH_ROUTES, trafficSeq);
    }


    /**
     * 获取交通工具信息
     *
     * @param trafficSeq 交通工具编号
     * @return
     */
    public PositionPoint getTrafficInfo(String trafficSeq) {
        return (PositionPoint) this.redisService.getCacheOfHash(RedisDataConstant.HASH_CAR, trafficSeq);
    }

    /**
     * 获取交通工具任务
     *
     * @param trafficSeq 交通工具序号
     * @return
     */
    public TrafficTask getTrafficTask(String trafficSeq) {
        return (TrafficTask) this.redisService.getCacheOfHash(RedisDataConstant.HASH_TASK, trafficSeq);
    }

    /**
     * 获取路径规划
     *
     * @param trafficSeq 交通工具序号
     * @return
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
    public NavPoint buildNavPoint(PositionPoint positionPoint, Integer endPointType, String portNo, double[] endPoint) {
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

    /**
     * 得到任务状态
     *
     * @return TaskStateInfo
     */
    public TaskStateInfo getTaskState(CarInfo carInfo) {
        return new TaskStateInfo();
    }

}
