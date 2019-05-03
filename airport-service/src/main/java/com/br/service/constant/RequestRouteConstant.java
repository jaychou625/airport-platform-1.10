package com.br.service.constant;

/**
 * 请求路径常量
 *
 * @Author Zero
 * @Date 2019 03 11
 */
public class RequestRouteConstant {
    /************************** resources dir **************************/
    // CSS资源文件路由常量
    public final static String REQUEST_ROUTE_CSS = "/css/**";
    // JS资源文件路由常量
    public final static String REQUEST_ROUTE_JS = "/js/**";
    // Image资源文件路由常量
    public final static String REQUEST_ROUTE_IMAGES = "/images/**";
    // WebSocket 路由常量
    public final static String REQUEST_ROUTE_WEBSOCKET = "/Wsocket";
    // MapResources 路由常量
    public final static String REQUEST_ROUTE_MAP_RESOURCES = "/map-resources/**";
    /************************** core dir **************************/
    // 登录页视图路由常量
    public final static String REQUEST_ROUTE_LOGIN = "/login";
    // 注销路由常量
    public final static String REQUEST_ROUTE_LOGOUT = "/logout";
    // 未授权视图路由常量
    public final static String REQUEST_ROUTE_UNAUTHED = "/unauthed";
    // 主页视图路由常量
    public final static String REQUEST_ROUTE_MAIN = "/main";
    // 首页视图路由常量
    public final static String REQUEST_ROUTE_HOME = "/home";
    /************************** sysops dir **************************/
    // 系统管理目录路由常量
    public final static String REQUEST_ROUTE_SYSOPS = "/sysops";
    // 车辆视图路由常量
    public final static String REQUEST_ROUTE_SYSOPS_CAR = "/car";
    // 车辆视图数据路由常量
    public final static String REQUEST_ROUTE_SYSOPS_CAR_LIST = "/list";
    // 单个车辆数据路由常量
    public final static String REQUEST_ROUTE_SYSOPS_FIND_CAR = "/find_car";
    /************************** monitor dir **************************/
    // 运营监管目录路由常量
    public final static String REQUEST_ROUTE_MONITOR = "/monitor";
    // 交通监管视图路由常量
    public final static String REQUEST_ROUTE_MONITOR_TRAFFIC = "/traffic";
    // 交通监管视图路由常量
    public final static String REQUEST_ROUTE_MONITOR_AEW = "/aew";
    // 交通监管数据路由常量
    public final static String REQUEST_ROUTE_MONITOR_AEW_List = "/list";
    /************************** mobile app dir **************************/
    // 车辆信息路由
    public final static String REQUEST_ROUTE_TRAFFIC_INFO = "/traffic/info";
    // 车辆信息路由
    public final static String REQUEST_ROUTE_TRAFFIC_TASK = "/traffic/task";
}
