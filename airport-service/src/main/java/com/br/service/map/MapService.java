package com.br.service.map;

import com.br.constant.RedisDataConstant;
import com.br.service.redis.RedisService;
import com.br.service.websocket.WSService;
import com.route.broadcast.PositionNotice;
import com.route.broadcast.RealNavServer;
import com.route.imp.NavEngine;
import com.route.imp.NavPoint;
import com.route.imp.PositionPoint;
import com.route.imp.nav.Distance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Vector;

/**
 * 路径规划服务
 *
 * @Author Zero
 * @Date 2019 02 22
 */

@Service("mapService")
public class MapService {

    // 导航引擎
    @Autowired
    private NavEngine navEngine;

    // 防撞服务
    @Autowired
    private RealNavServer realNavServer;

    // 文件流
    @Autowired
    private byte[] readMapToByte;

    // Redis工具类
    @Autowired
    private RedisService redisService;

    // 路径服务
    @Autowired
    private WSService wsService;


    /**
     * 单条路径规划
     *
     * @param navPoint 导航坐标
     * @return
     */
    public Distance routePlan(NavPoint navPoint) {
        if (navPoint != null) {
            Vector routeTasks = new Vector<>();
            if (routeTasks.add(navPoint)) {
                return (Distance) this.navEngine.searchPaths(routeTasks, this.readMapToByte, "4602000001").elementAt(0);
            }
        }
        return null;
    }


    /**
     * 计算交通工具工况
     *
     * @param trafficInfo 交通工具信息
     * @param distance    路径
     * @return Vector
     */
    public Vector<PositionNotice> calcWorkCondition(PositionPoint trafficInfo, Distance distance) {
        return this.realNavServer.calDeviceBroadcastPoint(trafficInfo, distance);
    }


    /**
     * 计算车辆不能到达区域
     *
     * @param trafficInfo 交通工具信息
     * @param distance    路径
     * @return Vector
     */
    public Vector<PositionNotice> calcEntryArea(PositionPoint trafficInfo, Distance distance) {
        return this.realNavServer.calDeviceNoEntryArea(trafficInfo, distance);
    }

    /**
     * 计算交通工具之间的冲突点
     *
     * @param trafficInfos 所有交通工具
     * @param routes       所有交通工具路径
     * @return
     */
    public Vector calcCrossConflict(Vector trafficInfos, Vector routes) {
        this.realNavServer.calCrossConflictPoints(trafficInfos, routes);
        return null;
    }


    /**
     * 计算车辆之间过近
     *
     * @param trafficInfos 所有交通工具
     * @param routes       所有交通工具路径
     * @return
     */
    public Vector calcNearCarConflict(Vector trafficInfos, Vector routes) {
        return this.realNavServer.calNearCarConflict(trafficInfos, routes);
    }


    /**
     * 获取所有路径规划
     *
     * @return Vector<Distance> 所有路径规划数组
     */
    public Vector<Distance> getAllRoutes() {
        Map<String, Distance> routeMap = this.redisService.getCacheOfHash(RedisDataConstant.HASH_ROUTES);
        Vector<Distance> trafficRoutes = new Vector<>();
        for (Distance route : routeMap.values()) {
            trafficRoutes.add(route);
        }
        trafficRoutes.sort((Distance r1, Distance r2) -> r1.deviceNo.compareToIgnoreCase(r2.deviceNo));
        return trafficRoutes;
    }

}
