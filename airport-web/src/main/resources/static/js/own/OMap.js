// 地图初始化
function mapInitialization(target, zoneId) {
    // 机场Id
    mapConfig.zoneId = zoneId;
    // 创建视图
    if (view === undefined || view === null) {
        view = new ol.View({
            center: mapConfig.center,
            zoom: mapConfig.zoom,
            minZoom: mapConfig.minZoom,
            maxZoom: mapConfig.maxZoom,
            projection: mapConfig.projection,
            maxResolution: mapConfig.maxResolution,
            rotation: mapConfig.rotation,
            extent: mapConfig.extent
        })
    }
    // 地图实例化
    map = new ol.Map({
        target: target,
        view: view
    })
    // 创建瓦片图层
    if (tileLayer === undefined || tileLayer === null) {
        tileLayer = new ol.layer.Tile({
            source: new ol.source.TileImage({
                tileUrlFunction: tileUrl,
                projection: mapConfig.projection,
                tileSize: mapConfig.tileSize,
                tileGrid: new ol.tilegrid.TileGrid({
                    resolutions: mapConfig.resolutions(),
                    tileSize: mapConfig.tileSize,
                    origin: [mapConfig.minLon, mapConfig.maxLat]
                })
            })
        })
    }
    // 添加瓦片图层到地图
    layerPlus(tileLayer)
    // 创建矢量图层
    if (vectorLayer === undefined || vectorLayer === null) {
        vectorLayer = new ol.layer.Vector({
            source: new ol.source.Vector()
        })
    }
    // 添加矢量图层到地图
    layerPlus(vectorLayer)
    // 添加控件
    controlPlus(new ol.control.ScaleLine())
}

// 瓦片路径计算
function tileUrl(tileCoords) {
    if (!tileCoords) return ""
    let z = tileCoords[0]
    let x = tileCoords[1]
    let y = tileCoords[2]
    let wn = mapConfig.zeroWidthNum * Math.pow(2, z)
    let bx = x / wn
    x = x % wn
    if (x * wn < 0 || bx >= 1) {
        x = 0
        y = 0
    } else {
        let hn = mapConfig.zeroHeightNum * Math.pow(2, z)
        let by = (y + 1) / hn
        if (y > 0 || by <= -1) {
            y = 0
            x = 0
        }
        y = -((y + 1) % hn)
    }
    let xStr = x.toString()
    let xStrLen = 4 - xStr.length
    for (let i = 0; i < xStrLen; i++) {
        xStr = "0" + xStr
    }
    let yStr = y.toString()
    let yStrLen = 4 - yStr.length
    for (let i = 0; i < yStrLen; i++) {
        yStr = "0" + yStr
    }
    z = Math.pow(2, z)
    // 瓦片路径
    let tileUrl = mapConfig.url + mapConfig.zoneId + "/" + z + "/" + z + yStr + xStr + ".png"
    return tileUrl
}