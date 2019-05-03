/*------------------------- 判断交通工具是否在机场里面 ----------------------------*/
function judgeTrafficIsInAirport(coords) {
    let extent = [109.3904249, 18.274809, 109.4342721, 18.329618]
    if (coords[0] >= extent[0] && coords[0] <= extent[2] && coords[1] >= extent[1] && coords[1] <= extent[3]) {
        return true
    }
    return false
}

/*------------------------- 构建或者刷新飞机对象 ----------------------------*/
function buildOrFlushPlanePositionObject(planes, source) {
    if (planes.length > 0) {
        $.each(planes, function (index, plane) {
            let planeFeature = getFeatureBySeq(source, "plane-" + plane.aircraftSeq)
            let coords = [plane.aircraftLongitude, plane.aircraftLatitude]
            if (judgeTrafficIsInAirport(coords)) {
                if (planeFeature != null) {
                    featurePosition(planeFeature, coords)
                    featureAttrs(planeFeature, "planeInfo", plane)
                    featureRotate(planeFeature, calcRad(plane.aircraftHeading, 97.4))
                } else {
                    planeFeature = drawPointFeature("plane", coords)
                    featureSeq(planeFeature, "plane-" + plane.aircraftSeq)
                    featureAttrs(planeFeature, "trafficType", "plane")
                    featureAttrs(planeFeature, "planeInfo", plane)
                    featureRotate(planeFeature, calcRad(plane.aircraftHeading, 97.4))
                    addFeature(source, planeFeature)
                }
            } else {
                removeFeature(source, planeFeature)
            }
        })
    }
}

/*------------------------- 构建或者刷新车辆对象 ----------------------------*/
function buildOrFlushTrafficPositionObject(car, source) {
    let carFeature = getFeatureBySeq(source, "car-" + car.deviceNo)
    let coords = [car.gpsPosition.iLongititude, car.gpsPosition.iLatitude]
    if (judgeTrafficIsInAirport(coords)) {
        if (carFeature != null) {
            featureAttrs("carInfo", car)
            featurePosition(carFeature, coords)
        } else {
            carFeature = drawPointFeature("car", coords)
            featureSeq(carFeature, "car-" + car.deviceNo)
            featureAttrs(carFeature, "trafficType", "car")
            featureAttrs(carFeature, "carInfo", car)
            addFeature(source, carFeature)
        }
    } else {
        removeFeature(source, carFeature)
    }
}

/*------------------------- 构建路径坐标点数组 ----------------------------*/
function buildRouteCoords(points) {
    let coordsArray = new Array()
    $.each(points, function (index1, coordsPoints) {
        $.each(coordsPoints, function (index2, coords) {
            coordsArray.push([coords.iLongititude, coords.iLatitude])
        })
    })
    return coordsArray
}

/*------------------------- 路径规划 ----------------------------*/
function routePlan(route, source) {
    let routeFeature = drawLineFeature(buildRouteCoords(route.list))
    featureSeq(routeFeature, "route-" + route.deviceNo)
    featureAttrs(routeFeature, "routeInfo", route)
    addFeature(source, routeFeature)
}

/*------------------------- 路径冲突点 ----------------------------*/
function routeCross(cross, source) {
    let coords = [cross.point.iLongititude, cross.point.iLatitude]
    let crossFeature = drawPointFeature("marker", coords);
    featureSeq(crossFeature, "cross-" + cross.deviceNo1)
    addFeature(source, crossFeature)
}

/*------------------------- 删除规划路径 ----------------------------*/
function removeRoute(source, routeId) {
    removeFeature(source, getFeatureBySeq(source, "route-" + routeId))
}

/*------------------------- 删除冲突点 ----------------------------*/
function removeCross(source, crossId) {
    removeFeature(source, getFeatureBySeq(source, "cross-" + crossId))
}

/*------------------------- 计算方向旋转弧度 注意: verticalAngle 垂直角度指的是与北方向形成的夹角角度 ----------------------------*/
function calcRad(angle, verticalAngle) {
    return Math.PI / 180 * (angle + verticalAngle)
}