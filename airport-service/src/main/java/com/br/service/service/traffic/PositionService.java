package com.br.service.service.traffic;

import com.alibaba.fastjson.JSON;
import com.br.entity.map.Plane;
import com.br.entity.websocket.WSMessage;
import com.br.service.constant.RedisDataConstant;
import com.br.service.constant.WSMessageConstant;
import com.br.service.service.redis.RedisService;
import com.br.service.service.websocket.WSService;
import com.route.imp.PositionPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * 交通工具位置服务
 *
 * @Author Zero
 * @Date 2019 03 27
 */
@Service
public class PositionService {

    // WebSocket 服务
    @Autowired
    private WSService wsService;

    // Redis 服务
    @Autowired
    private RedisService redisService;

    // 交通工具任务服务
    @Autowired
    private TrafficTaskService trafficTaskService;


    /**
     * 记录飞机的实时位置
     *
     * @param planes 飞机信息集合
     */
    public void savePlanesInfo(List<Plane> planes) {
        if (planes != null && planes.size() > 0) {
            Map<String, Object> aDSBInfosMap = new HashMap<>();
            for (Plane aDSBInfo : planes) {
                aDSBInfosMap.put(aDSBInfo.getAircraftSeq(), aDSBInfo);
            }
            this.redisService.saveCacheOfHash(RedisDataConstant.HASH_PLANE, aDSBInfosMap);
            this.sendPlanePositionObject(planes);
        }
    }

    /**
     * WebSocket 发送飞机位置信息
     */
    public void sendPlanePositionObject(List<Plane> planes) {
        this.wsService.wsSend(new WSMessage(WSMessageConstant.EVENT_PLANE_POSITION, JSON.toJSONString(planes)));
    }


    /**
     * 记录交通工具的实时位置
     *
     * @param trafficInfo 交通工具位置信息
     */
    public void saveTrafficInfo(PositionPoint trafficInfo) {
        this.redisService.saveCacheOfHash(RedisDataConstant.HASH_CAR, trafficInfo.deviceNo, trafficInfo);
        if (this.trafficTaskService.hasTask(trafficInfo.deviceNo) && this.trafficTaskService.hasRoute(trafficInfo.deviceNo)) {
            this.trafficTaskService.trafficAewInfoHandler(trafficInfo);
        }
        this.sendTrafficPositionObject(trafficInfo);
    }

    /**
     * 发送交通工具位置信息
     */
    public void sendTrafficPositionObject(PositionPoint trafficInfo) {
        this.wsService.wsSend(new WSMessage(WSMessageConstant.EVENT_CAR_POSITION, JSON.toJSONString(trafficInfo)));
    }


    /**
     * 得到交通工具位置信息
     *
     * @param trafficSeq 交通工具序号
     * @return
     */
    public PositionPoint getTrafficInfo(String trafficSeq) {
        return (PositionPoint) this.redisService.getCacheOfHash(RedisDataConstant.HASH_CAR, trafficSeq);
    }

    /**
     * 得到飞机位置信息
     *
     * @param aircraftSeq
     * @return
     */
    public Plane getPlaneInfo(String aircraftSeq) {
        return (Plane) this.redisService.getCacheOfHash(RedisDataConstant.HASH_PLANE, aircraftSeq);
    }


    /**
     * 获取所有交通工具信息
     *
     * @return Vector<PositionPoint>
     */
    public Vector<PositionPoint> getAllTrafficInfos() {
        Map<String, PositionPoint> trafficMap = this.redisService.getCacheOfHash(RedisDataConstant.HASH_CAR);
        Vector<PositionPoint> routes = new Vector<>();
        for (PositionPoint trafficInfo : trafficMap.values()) {
            routes.add(trafficInfo);
        }
        routes.sort((PositionPoint p1, PositionPoint p2) -> p1.deviceNo.compareToIgnoreCase(p2.deviceNo));
        return routes;
    }


}
