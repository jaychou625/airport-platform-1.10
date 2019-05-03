/*------------------------- 绘制线条要素 ----------------------------*/
function drawLineFeature(coords) {
    let geometry = new ol.geom.LineString(coords)
    let lineFeature = new ol.Feature({
        type: "route",
        geometry: geometry
    })
    lineFeature.setStyle(new ol.style.Style({
        stroke: new ol.style.Stroke({
            width: 5,
            color: '#32CD99'
        })
    }))
    return lineFeature
}

/*------------------------- 绘制点要素 ----------------------------*/
function drawPointFeature(markerType, coords) {
    let geometry = new ol.geom.Point(coords)
    let vehicleFeature = new ol.Feature({
        geometry: geometry
    })
    vehicleFeature.setStyle(new ol.style.Style({
        image: new ol.style.Icon({
            anchor: [1, 0.5],
            scale: 0.3,
            src: "/images/map-images/" + markerType + ".png"
        })
    }))
    return vehicleFeature
}

/*------------------------- 绘制弹出要素 ----------------------------*/
function drawPopupFeature(container) {
    return new ol.Overlay({
        element: container,
        autoPan: true,
        autoPanAnimation: {
            duration: 250
        }
    })
}

/*------------------------- 要素添加 ----------------------------*/
function addFeature(source, feature) {
    source.addFeatures([feature])
}

/*------------------------- 设置要素序号 ----------------------------*/
function featureSeq(feature, featureSeq) {
    feature.setId(featureSeq)
}

/*------------------------- 添加要素信息 ----------------------------*/
function featureAttrs(feature, attrName, attrValue) {
    feature.set(attrName, attrValue)
}

/*------------------------- 通过序号获取要素 ----------------------------*/
function getFeatureBySeq(source, featureSeq) {
    let feature = source.getFeatureById(featureSeq)
    return feature
}

/*------------------------- 获取当前要素坐标 ----------------------------*/
function getFeatureCoords(feature) {
    return feature.getGeometry().getCoordinates()
}

/*------------------------- 设置当前要素坐标 ----------------------------*/
function featurePosition(feature, coords) {
    feature.getGeometry().setCoordinates(coords)
}

/*------------------------- 要素旋转 ----------------------------*/
function featureRotate(feature, rad) {
    feature.style_.image_.rotation_ = rad
}

/*------------------------- 删除要素 ----------------------------*/
function removeFeature(source, feature) {
    if (feature != null) {
        source.removeFeature(feature)
    }
}

/*------------------------- 地图控件添加 ----------------------------*/
function controlPlus(control) {
    map.addControl(control)
}

/*------------------------- 地图图层添加 ----------------------------*/
function layerPlus(layer) {
    map.addLayer(layer)
}

