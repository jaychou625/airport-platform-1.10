function mapInitialization(target) {
    /*------------------------- 创建视图 ----------------------------*/
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
    /*------------------------- 地图实例化 ----------------------------*/
    map = new ol.Map({
        target: target,
        view: view
    })
    /*------------------------- 瓦片图层 ----------------------------*/
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
    layerPlus(tileLayer)
    /*------------------------- 适量图层 ----------------------------*/
    if (vectorLayer === undefined || vectorLayer === null) {
        vectorLayer = new ol.layer.Vector({
            source: new ol.source.Vector()
        })
    }
    layerPlus(vectorLayer)
    controlPlus(new ol.control.ScaleLine())

    map.render()
}

/*------------------------- 瓦片算法 ----------------------------*/
function tileUrl(tileCoord, pixelRatio, projection) {
    if (!tileCoord) return "";
    var z = tileCoord[0];
    var x = tileCoord[1];
    var y = tileCoord[2];
    var wn = mapConfig.zeroWidthNum * Math.pow(2, z);
    var bx = x / wn;
    x = x % wn;
    if (x * wn < 0 || bx >= 1) {
        x = 0;
        y = 0;
    } else {
        var hn = mapConfig.zeroHeightNum * Math.pow(2, z);
        var by = (y + 1) / hn;
        if (y > 0 || by <= -1) {
            y = 0;
            x = 0;
        }
        y = -((y + 1) % hn);
    }
    var xStr = x.toString();
    var xStrLen = 4 - xStr.length;
    for (var i = 0; i < xStrLen; i++) {
        xStr = "0" + xStr;
    }
    var yStr = y.toString();
    var yStrLen = 4 - yStr.length;
    for (var i = 0; i < yStrLen; i++) {
        yStr = "0" + yStr;
    }
    z = Math.pow(2, z);
    let tileUrl = mapConfig.url + mapConfig.zoneId + "/" + z + "/" + z + yStr + xStr + ".png"
    return tileUrl
}