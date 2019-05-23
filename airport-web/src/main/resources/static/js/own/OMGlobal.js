/*------------------------- 服务器IP与端口 ----------------------------*/
let serverIp = "127.0.0.1"
let serverPort = "9090"
/*------------------------- 地图 ----------------------------*/
let map
// 地图视图
let view
// 瓦片图层
let tileLayer
// 地图配置
let mapConfig = {
    zoneId: "4602000001", // 机场Id
    url: "http://" + serverIp + ":" + serverPort + "/map-resources/", // 地图链接
    center: [109.4123485, 18.3022135], // 地图中点
    zoom: 0, // 当前视图等级
    minZoom: 0, // 最小视图等级
    maxZoom: 3, // 最大视图等级
    projection: "EPSG:4326", // 投影坐标系类型
    maxResolution: 0.0000261890625, // 最大分辨率
    maxLon: 109.4340271, // 最大经度
    maxLat: 18.323258, // 最大纬度
    minLon: 109.3803919, // 最小经度
    minLat: 18.289736, // 最小纬度
    rotation: 0.13, // 地图旋转弧度
    extent: [109.3803919, 18.289736, 109.4340271, 18.323258], // 边界坐标
    tileSize: [256, 256], // 瓦片宽高
    zeroWidthNum: 8,
    zeroHeightNum: 5,
    resolutions: function () {  // 瓦片分辨率计算
        let resolutions_array = new Array(4);
        for (let i = 0; i <= this.maxZoom; i++) {
            resolutions_array[i] = this.maxResolution / Math.pow(2, i)
        }
        return resolutions_array
    }
}
// websocket变量
let websocket
// 矢量图层
let vectorLayer
// 事件常量
let wsEvents = {
    EVENT_PLANE_POSITION: "EVENT_PLANE_POSITION",
    EVENT_CAR_POSITION: "EVENT_CAR_POSITION",
    EVENT_ROUTE_PLAN: "EVENT_ROUTE_PLAN",
    EVENT_ROUTE_CROSS: "EVENT_CROSS_POINT",
    EVENT_CLEAN: "EVENT_CLEAN"
}