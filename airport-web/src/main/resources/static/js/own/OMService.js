// 构建或者刷新航空器对象
function buildOrFlushPlanePositionObject(planePositionObjects, source) {
    if (planePositionObjects.length > 0) {
        $.each(planePositionObjects, function (index, planePositionObject) {
            let planeFeature = getFeatureBySeq(source, "plane-" + planePositionObject.aircraftSeq)
            let coords = [planePositionObject.aircraftLongitude, planePositionObject.aircraftLatitude]
            let isPlaneInAirport = judgePlaneIsInAirport(coords);
            if (planeFeature != null) {
                if (isPlaneInAirport) {
                    planeFeature.set("planeInfo", planePositionObject)
                    featurePosition(planeFeature, coords)
                    featureRotate(planeFeature, calcRad(planePositionObject.aircraftHeading, 97.4))
                } else {
                    removeFeature(source, planeFeature)
                }
            } else {
                if (isPlaneInAirport) {
                    planeFeature = drawPointFeature("plane", coords)
                    planeFeature.setId("plane-" + planePositionObject.aircraftSeq)
                    planeFeature.set("trafficType", "plane")
                    planeFeature.set("planeInfo", planePositionObject)
                    featureRotate(planeFeature, calcRad(planePositionObject.aircraftHeading, 97.4))
                    addFeature(source, planeFeature)
                }
            }

        })
    }
}


// 构建或者刷新车辆对象
function buildOrFlushTrafficPositionObject(trafficPositionObject, source) {
    if (trafficPositionObject != null) {
        let trafficFeature = getFeatureBySeq(source, "car-" + trafficPositionObject.deviceNo)
        let coords = [trafficPositionObject.gpsPosition.iLongititude, trafficPositionObject.gpsPosition.iLatitude]
        if (trafficFeature != null) {
            trafficFeature.set("carInfo", trafficPositionObject)
            featurePosition(trafficFeature, coords)
        } else {
            trafficFeature = drawPointFeature("car", coords)
            trafficFeature.setId("car-" + trafficPositionObject.deviceNo)
            trafficFeature.set("trafficType", "car")
            trafficFeature.set("carInfo", trafficPositionObject)
            addFeature(source, trafficFeature)
        }
    }
}

// 路径规划
function routePlan(route, source) {
    let coordsArray = new Array()
    $.each(route.list, function (index, coordsObject) {
        $.each(coordsObject.points, function (index2, coords) {
            coordsArray.push([coords.iLongititude, coords.iLatitude])
        })
    })
    let routeFeature = drawLineFeature(coordsArray)
    routeFeature.setId("route-" + route.deviceNo)
    addFeature(source, routeFeature)
}


// 冲突点
function routeCross(cross, source) {
    let coords = [cross.point.iLongititude, cross.point.iLatitude]
    let crossFeature = drawPointFeature("marker", coords);
    crossFeature.setId("cross-" + cross.deviceNo1);
    addFeature(source, crossFeature)
}

// 删除路径
function removeRoute(source, routeId) {
    let routeFeature = getFeatureBySeq(source, "route-" + routeId)
    source.removeFeature(routeFeature)
}

// 删除冲突点
function removeCross(source, crossId) {
    let crossFeature = getFeatureBySeq(source, "cross-" + crossId)
    source.removeFeature(crossFeature)
}


// 计算方向旋转弧度 注意: verticalAngle 垂直角度指的是与北方向形成的夹角角度
function calcRad(angle, verticalAngle) {
    return Math.PI / 180 * (angle + verticalAngle)
}

// 判断飞机是否在机场里面
function judgePlaneIsInAirport(coords) {
    let extent = [109.3904249, 18.274809, 109.4342721, 18.329618]
    if (coords[0] >= extent[0] && coords[0] <= extent[2] && coords[1] >= extent[1] && coords[1] <= extent[3]) {
        return true
    }
    return false
}

// 清除无效飞机对象
function clearInValidPlane(source, planePositionObjects) {
    let features = source.getFeatures()
    $.each(features, function (index, feature) {
        if (feature.get("trafficType") == "plane") {
            let planeId = feature.getId().split("-")[1]
            let exists = false
            $.each(planePositionObjects, function (index2, planePositionObject) {
                if (planeId === planePositionObject.aircraftSeq) {
                    exists = true
                }
            })
            if (!exists) {
                removeFeature(source, feature)
            }
        }
    })
}