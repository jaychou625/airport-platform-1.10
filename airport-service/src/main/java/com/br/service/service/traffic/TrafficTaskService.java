package com.br.service.service.traffic;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.br.entity.map.AewInfo;
import com.br.entity.map.TrafficTask;
import com.br.entity.websocket.WSMessage;
import com.br.service.constant.RedisDataConstant;
import com.br.service.constant.WSMessageConstant;
import com.br.service.service.map.MapService;
import com.br.service.service.redis.RedisService;
import com.br.service.service.websocket.WSService;
import com.br.service.utils.CrypUtils;
import com.route.broadcast.ConflictPoint;
import com.route.broadcast.PositionNotice;
import com.route.imp.NavPoint;
import com.route.imp.PositionPoint;
import com.route.imp.nav.Distance;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
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

    // 密码工具类
    @Autowired
    private CrypUtils crypUtils;


    /**
     * 记录任务
     *
     * @param trafficTask 交通工具任务实例
     */

    public void saveTrafficTask(TrafficTask trafficTask, boolean isReRoute) {
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
    }

    /**
     * 车辆路径规划
     *
     * @param trafficTask 交通工具任务
     * @param trafficInfo 交通工具信息
     */
    public void carTaskHandler(TrafficTask trafficTask, PositionPoint trafficInfo, boolean isReRoute) {
        NavPoint navPoint;
        Distance distance = null;
        switch (trafficTask.getTaskEvent()) {
            case "start_task":
                if (isReRoute) {
                    this.redisService.removeCacheOfHash(RedisDataConstant.HASH_ROUTES, trafficInfo.deviceNo);
                    this.wsService.wsSend(new WSMessage(WSMessageConstant.EVENT_RE_ROUTE_PLAN, JSON.toJSONString(trafficTask.getCarSeq())));
                }
                navPoint = buildNavPoint(trafficInfo, trafficTask, 1, 1);
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
    }

    /* *
     * 当前交通工具的预警计算
     *
     * @param trafficInfo
     */
    public void trafficAewInfoHandler(PositionPoint trafficInfo) {
        if (this.hasTask(trafficInfo.deviceNo) && this.hasRoute(trafficInfo.deviceNo)) {
            TrafficTask trafficTask = this.getTrafficTask(trafficInfo.deviceNo);
            Distance trafficRoute = this.getRoutePlan(trafficInfo.deviceNo);
            Vector<PositionNotice> workConditions = this.mapService.calcWorkCondition(trafficInfo, trafficRoute);
            if (workConditions != null && workConditions.size() > 0) {
                for (PositionNotice positionNotice : workConditions) {
                    if (positionNotice.noticeType == PositionNotice.TYPE_FARAWAY) {
                        this.saveTrafficTask(trafficTask, true);
                    }else{
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
    }


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
     * 获取导航坐标
     *
     * @param positionPoint      交通工具信息
     * @param trafficTask        交通工具任务
     * @param targetPositionType 目标地址类型
     * @return
     */
    public NavPoint buildNavPoint(PositionPoint positionPoint, TrafficTask trafficTask, Integer endPointType, Integer targetPositionType) {
        NavPoint navPoint = new NavPoint();
        navPoint.zoneID = "4602000001";
        navPoint.deviceNo = positionPoint.deviceNo;
        navPoint.deviceType = positionPoint.deviceType;
        navPoint.startPoint.iLongititude = positionPoint.gpsPosition.iLongititude;
        navPoint.startPoint.iLatitude = positionPoint.gpsPosition.iLatitude;
        navPoint.endPointType = endPointType;
        if (targetPositionType == 1) {
            navPoint.endPointName = trafficTask.getTargetOfPointName();
        } else if (targetPositionType == 2) {
            navPoint.endPoint.iLongititude = trafficTask.getTargetOfPoint()[0].doubleValue();
            navPoint.endPoint.iLatitude = trafficTask.getTargetOfPoint()[1].doubleValue();
        }
        return navPoint;
    }


    /*****************************获取站坪数据*****************************/
    public void getApronData(){
        Date date = new Date();
        SimpleDateFormat sdf_date = new SimpleDateFormat("yyyyMMdd");
        String dateString = sdf_date.format(date);
        SimpleDateFormat sdf_datetime = new SimpleDateFormat("yyyyMMddHHmmss");
        String datetimeString = sdf_datetime.format(date);
        String str = "Datasyx" + dateString + datetimeString + "syx.call.2019wgss.webcall.2019";
        String sign = this.crypUtils.toMD5(str);
        String url = "http://10.2.135.122:8091/CallHandlers/WgssHandler.ashx?MethodName=Data&CallUserName=syx&PlanDate=" + dateString + "&DateTimeToken=" + datetimeString + "&Sign=" + sign;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            JSONObject jsonObject = JSONObject.parseObject(response.body().string());
            // 格式化JSON数据
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }


}
