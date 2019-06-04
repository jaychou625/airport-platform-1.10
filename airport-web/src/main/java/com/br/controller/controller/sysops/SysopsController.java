package com.br.controller.controller.sysops;

import com.br.constant.RequestRouteConstant;
import com.br.constant.ViewConstant;
import com.br.constant.enumeration.CommonEnumeration;
import com.br.entity.map.Car;
import com.br.entity.utils.BreadCrumb;
import com.br.entity.utils.Result;
import com.br.log.annotation.BizOperation;
import com.br.service.traffic.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 交通工具控制器
 *
 * @Author Zero
 * @Date 2019 02 21
 */
@Controller
public class SysopsController {

    // 汽车服务
    @Autowired
    private CarService carService;

    /**
     * 车辆视图页
     *
     * @param model 响应结果集
     * @return 视图
     */
    @BizOperation("请求车辆视图")
    @RequestMapping(value = RequestRouteConstant.REQUEST_ROUTE_SYSOPS + RequestRouteConstant.REQUEST_ROUTE_SYSOPS_CAR, method = RequestMethod.GET)
    public String carPage(Model model) {
        Result result = new Result();
        result.setStatus(CommonEnumeration.SUCCESS.getStatus());
        result.setCode(CommonEnumeration.SUCCESS.getCode());
        result.getData().put("breadcrumb", new BreadCrumb[]{new BreadCrumb("首页", RequestRouteConstant.REQUEST_ROUTE_SYSOPS + RequestRouteConstant.REQUEST_ROUTE_SYSOPS_CAR), new BreadCrumb("车辆基本信息", RequestRouteConstant.REQUEST_ROUTE_SYSOPS_CAR)});
        model.addAttribute("result", result);
        return ViewConstant.VIEW_DIR_SYSOPS + ViewConstant.VIEW_FILE_SYSOPS_CAR;
    }

    /**
     * 单个车辆信息
     *
     * @param carSeq 车辆序号
     * @return Car
     */
    @BizOperation("请求单个车辆信息")
    @RequestMapping(value = RequestRouteConstant.REQUEST_ROUTE_SYSOPS + RequestRouteConstant.REQUEST_ROUTE_SYSOPS_CAR + RequestRouteConstant.REQUEST_ROUTE_SYSOPS_FIND_CAR, method = RequestMethod.GET)
    @ResponseBody
    public Car car(@RequestParam("carSeq") Integer carSeq) {
        System.out.println(carSeq);
        return this.carService.find(carSeq);
    }


}
