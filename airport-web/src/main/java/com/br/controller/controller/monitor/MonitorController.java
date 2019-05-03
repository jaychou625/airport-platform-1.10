package com.br.controller.controller.monitor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.br.entity.map.TrafficTask;
import com.br.entity.utils.BreadCrumb;
import com.br.entity.utils.Result;
import com.br.service.constant.RequestRouteConstant;
import com.br.service.constant.ViewConstant;
import com.br.service.enumeration.CommonEnumeration;
import com.br.service.service.traffic.AewService;
import com.br.service.service.traffic.PositionService;
import com.br.service.service.traffic.TrafficTaskService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.route.imp.PositionPoint;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 地图服务控制器
 *
 * @Author Zero
 * @Date 2019 02 22
 */
@Controller
public class MonitorController {

    // 位置服务
    @Autowired
    private PositionService positionService;

    // 任务服务
    @Autowired
    private TrafficTaskService trafficTaskService;

    // 预警信息服务
    @Autowired
    private AewService aewService;

    /**
     * 预警监管视图
     *
     * @param model 响应结果集
     * @return 视图
     */
    @RequiresPermissions(RequestRouteConstant.REQUEST_ROUTE_MONITOR + RequestRouteConstant.REQUEST_ROUTE_MONITOR_AEW)
    @RequestMapping(value = RequestRouteConstant.REQUEST_ROUTE_MONITOR + RequestRouteConstant.REQUEST_ROUTE_MONITOR_AEW, method = RequestMethod.GET)
    public String aewPage(Model model) {
        model.addAttribute("breadcrumb", new BreadCrumb[]{new BreadCrumb("首页", RequestRouteConstant.REQUEST_ROUTE_HOME), new BreadCrumb("预警监管", RequestRouteConstant.REQUEST_ROUTE_MONITOR + RequestRouteConstant.REQUEST_ROUTE_MONITOR_AEW)});
        return ViewConstant.VIEW_DIR_MONITOR + ViewConstant.VIEW_FILE_MONITOR_AEW;
    }

    /**
     * /**
     * 预警监管数据集合
     *
     * @param currentPage 当前页
     * @param pageSize    页面数量
     * @return List<AewInfo>
     */
    @RequestMapping(value = RequestRouteConstant.REQUEST_ROUTE_MONITOR + RequestRouteConstant.REQUEST_ROUTE_MONITOR_AEW + RequestRouteConstant.REQUEST_ROUTE_MONITOR_AEW_List, method = RequestMethod.GET)
    @ResponseBody
    public Result aewList(@RequestParam("currentPage") Integer currentPage, @RequestParam("pageSize") Integer pageSize) {
        PageHelper.startPage(currentPage, pageSize, true);
        PageInfo pageInfo = new PageInfo(this.aewService.findAll());
        Result result = new Result();
        result.setStatus(CommonEnumeration.SUCCESS.getStatus());
        result.setCode(CommonEnumeration.SUCCESS.getCode());
        result.getData().put("pageInfo", pageInfo);
        return result;
    }

    /**
     * 交通监管
     *
     * @param model 响应结果集
     * @return 视图
     */
    @RequiresPermissions({RequestRouteConstant.REQUEST_ROUTE_MONITOR + RequestRouteConstant.REQUEST_ROUTE_MONITOR_TRAFFIC})
    @RequestMapping(value = RequestRouteConstant.REQUEST_ROUTE_MONITOR + RequestRouteConstant.REQUEST_ROUTE_MONITOR_TRAFFIC, method = RequestMethod.GET)
    public String trafficPage(Model model) {
        model.addAttribute("zoneId", "4602000001");
        model.addAttribute("breadcrumb", new BreadCrumb[]{new BreadCrumb("首页", RequestRouteConstant.REQUEST_ROUTE_HOME), new BreadCrumb("交通监管", RequestRouteConstant.REQUEST_ROUTE_MONITOR + RequestRouteConstant.REQUEST_ROUTE_MONITOR_TRAFFIC)});
        return ViewConstant.VIEW_DIR_MONITOR + ViewConstant.VIEW_FILE_MONITOR_TRAFFIC;
    }


    /**
     * 交通工具信息回传
     *
     * @param trafficInfo 交通工具信息字符串
     */
    @RequestMapping(value = RequestRouteConstant.REQUEST_ROUTE_MONITOR + RequestRouteConstant.REQUEST_ROUTE_TRAFFIC_INFO, method = RequestMethod.POST)
    @ResponseBody
    public void trafficInfo(@RequestBody String trafficInfo) {
        JSONObject jsonObject = JSON.parseObject(trafficInfo);
        PositionPoint positionPoint = new PositionPoint();
        positionPoint.deviceNo = jsonObject.getInteger("car_seq").toString();
        positionPoint.deviceType = "car";
        positionPoint.carType = jsonObject.getInteger("car_type_seq");
        positionPoint.gpsPosition.iLongititude = jsonObject.getDouble("car_lon");
        positionPoint.gpsPosition.iLatitude = jsonObject.getDouble("car_lat");
        this.positionService.saveTrafficInfo(positionPoint);
        this.trafficTaskService.trafficAewInfoHandler(positionPoint);
    }


    /**
     * 交通工具任务
     *
     * @param trafficTaskOfString 交通工具任务字符串
     */
    @RequestMapping(value = RequestRouteConstant.REQUEST_ROUTE_MONITOR + RequestRouteConstant.REQUEST_ROUTE_TRAFFIC_TASK, method = RequestMethod.POST)
    @ResponseBody
    public void trafficTask(@RequestBody String trafficTaskOfString) {
        JSONObject jsonObject = JSON.parseObject(trafficTaskOfString);
        String task_event = jsonObject.getString("task_event");
        Integer car_seq = jsonObject.getInteger("car_seq");
        String target_name = jsonObject.getString("target_name");
        String plane_seq = jsonObject.getString("plane_seq");
        TrafficTask trafficTask = new TrafficTask();
        trafficTask.setTaskEvent(task_event);
        trafficTask.setTargetOfPointName(target_name);
        trafficTask.setPlaneSeq(plane_seq);
        trafficTask.setCarSeq(car_seq.toString());
        this.trafficTaskService.saveTrafficTask(trafficTask, false);
    }


}