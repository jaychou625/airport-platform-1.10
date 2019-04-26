// 地图实例
let map
// 地图视图
let view
// 瓦片图层
let tileLayer
// 地图配置
let mapConfig = {
    zoneId: "", // 机场Id
    url: "http://localhost:9090/map-resources/", // 地图链接
    center: [109.4123485, 18.3022135], // 地图中点
    zoom: 0, // 当前视图等级
    minZoom: 0, // 最小视图等级
    maxZoom: 3, // 最大视图等级
    projection: "EPSG:4326", // 投影坐标系类型
    maxResolution: 0.00004281953125, // 最大分辨率
    maxLon: 109.4342721, // 最大经度
    maxLat: 18.329618, // 最大纬度
    minLon: 109.3904249, // 最小经度
    minLat: 18.274809, // 最小纬度
    rotation: 0.13, // 地图旋转弧度
    extent: [109.3904249, 18.274809, 109.4342721, 18.329618], // 边界坐标
    tileSize: [256, 256], // 瓦片宽高
    zeroWidthNum: 4,
    zeroHeightNum: 5,
    resolutions:  function() {  // 瓦片分辨率计算
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