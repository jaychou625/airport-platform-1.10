package com.br.service.service.traffic;

import com.alibaba.fastjson.JSON;
import com.br.entity.map.Plane;
import com.br.entity.websocket.WSMessage;
import com.br.mapper.PlaneMapper;
import com.br.service.constant.RedisDataConstant;
import com.br.service.constant.WSMessageConstant;
import com.br.service.service.redis.RedisService;
import com.br.service.service.websocket.WSService;
import com.route.imp.PositionPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
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

    // Plane Mapper
    @Resource
    private PlaneMapper planeMapper;


    /**
     * 记录飞机的实时位置
     *
     * @param planes 飞机信息对象集合
     */
    public void savePlanesInfo(List<Plane> planes) {
        if (planes != null && planes.size() > 0) {
            this.sendPlanePositionObject(planes);
            Map<String, Object> planeMap = new HashMap<>();
            for (Plane plane : planes) {
                String planeSeq = plane.getPlaneSeq();
                if (StringUtils.isEmpty(planeSeq)) {
                    planeMap.put(plane.getPlaneAddrCode(), plane);
                } else {
                    planeMap.put(plane.getPlaneSeq(), plane);
                }
                this.planeMapper.add(plane);
            }
            this.redisService.saveCacheOfHash(RedisDataConstant.HASH_PLANE, planeMap);
        }
    }

    /**
     * WebSocket 发送飞机信息
     *
     * @param planes 飞机信息对象集合
     */
    public void sendPlanePositionObject(List<Plane> planes) {
        this.wsService.wsSend(new WSMessage(WSMessageConstant.EVENT_PLANE_POSITION, JSON.toJSONString(planes)));
    }


    /**
     * 记录交通工具的实时位置
     *
     * @param carInfo 交通工具位置信息
     */
    public void saveCarInfo(PositionPoint carInfo) {
        this.sendCarPositionObject(carInfo);
        this.redisService.saveCacheOfHash(RedisDataConstant.HASH_CAR, carInfo.deviceNo, carInfo);
    }

    /**
     * 发送交通工具位置信息
     *
     * @param carInfo
     */
    public void sendCarPositionObject(PositionPoint carInfo) {
        this.wsService.wsSend(new WSMessage(WSMessageConstant.EVENT_CAR_POSITION, JSON.toJSONString(carInfo)));
    }

    /**
     * 获取车辆位置信息
     *
     * @param carNo 车辆号码牌
     * @return CarPosition 车辆位置对象
     */
    public PositionPoint getCarInfo(String carNo) {
        return (PositionPoint) this.redisService.getCacheOfHash(RedisDataConstant.HASH_CAR, carNo);
    }

    /**
     * 获取飞机位置信息
     *
     * @param planeAddrCode 飞机地址码
     * @return PlanePosition 飞机位置对象
     */
    public Plane getPlaneInfo(String planeAddrCode) {
        return (Plane) this.redisService.getCacheOfHash(RedisDataConstant.HASH_PLANE, planeAddrCode);
    }


    /**
     * 获取所有车辆位置信息
     *
     * @return Vector<PositionPoint> 车辆位置信息集合
     */
    public Vector<PositionPoint> getAllCarsInfo() {
        Map<String, PositionPoint> carMap = this.redisService.getCacheOfHash(RedisDataConstant.HASH_CAR);
        Vector<PositionPoint> carPositions = new Vector<>();
        for (PositionPoint carPosition : carMap.values()) {
            carPositions.add(carPosition);
        }
        carPositions.sort((PositionPoint p1, PositionPoint p2) -> p1.deviceNo.compareToIgnoreCase(p2.deviceNo));
        return carPositions;
    }

    /**
     * 获取所有飞机位置信息
     *
     * @return Vector<PositionPoint> 飞机位置信息集合
     */
    public Vector<PositionPoint> getAllPlanesInfo() {
        Map<String, Plane> planeMap = this.redisService.getCacheOfHash(RedisDataConstant.HASH_PLANE);
        Vector<PositionPoint> planePositions = new Vector<>();
        for (String planeSeq : planeMap.keySet()) {
            Plane plane = planeMap.get(planeSeq);
            PositionPoint planePosition = new PositionPoint();
            planePosition.deviceNo = planeSeq;
            planePosition.deviceType = "plane";
            planePosition.gpsPosition.iLongititude = plane.getPlaneLongitude().doubleValue();
            planePosition.gpsPosition.iLatitude = plane.getPlaneLatitude().doubleValue();
            planePositions.add(planePosition);
        }
        planePositions.sort((PositionPoint p1, PositionPoint p2) -> p1.deviceNo.compareToIgnoreCase(p2.deviceNo));
        return planePositions;
    }

}
