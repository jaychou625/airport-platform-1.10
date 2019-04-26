// tips信息字符串模版
function tipsStringTemplate(tipsInfo) {
    // 模拟任务信息
    let taskInfo = {
        deviceId: tipsInfo.deviceId,
        endPointType: 1,
        zoneId: "4602000001"
    };
    if (tipsInfo.deviceType === "sysops") {
        taskInfo.endPointName = 902;
        taskInfo.endLonPoint = 109.422;
        taskInfo.endLatPoint = 18.307
    } else if (tipsInfo.deviceType === "plane") {
        taskInfo.endPointName = 113;
        taskInfo.endLonPoint = 109.422;
        taskInfo.endLatPoint = 18.307
    } else {
        return
    }
    taskInfo = JSON.stringify(taskInfo).replace(/\"/g, "'");
    let stringTemplate = "<div class='out'></div>" +
        "<div class='in'></div>" +
        "<div class='infoBar'>" +
        "<div class='infoTitle'>Infos:</div>" +
        "<div class='infoClose' onclick=\"closeTips('" + tipsInfo.tipsId + "')\">x</div>" +
        "</div>" +
        "<ul class='infos'>" +
        "  <li>deviceId: " + tipsInfo.deviceId + "</li>" +
        "  <li>deviceType: " + tipsInfo.deviceType + "</li>" +
        "  <li>speed: 40km/h</li>" +
        "</ul>" +
        "<button class='btn_plan' onclick=\"routePlan(" + taskInfo + ")\">路径规划</button>" +
        "<button class='btn_track' onclick=\"vehicleAnimate('" + tipsInfo.deviceId + "')\">轨迹追踪</button>";
    return stringTemplate
}

// 关闭销毁tips
function closeTips(tipsId) {
    let currentTips = $("#" + tipsId);
    // 删除当前tips元素节点
    currentTips.remove()
}

// 打开与关闭tips
function ocTips(element, feature) {
// 克隆弹出元素
    let tipsTemp = element.cloneNode(true);
    // 更改克隆元素的ID
    $(tipsTemp).attr("id", "VehicleInfo_" + feature.getId());
    // 设置Tips内容
    $(tipsTemp).html(tipsStringTemplate({
        tipsId: "VehicleInfo_" + feature.getId(),
        deviceId: feature.getId(),
        deviceType: feature.get("deviceType"),
        feature: feature
    }));
    // 显示Tips
    $(tipsTemp).css("display", "block");
    // 创建弹出Tips
    let popupTips = createTipsOverlay(tipsTemp);
    // 添加弹出Tips到地图
    layerPlus("over", popupTips);
    // 设置弹出Tips的位置
    popupTips.setPosition(feature.getGeometry().getCoordinates())
}


/*
 // 交通设备转向计算
 function calcRotate(coords, trackIndex) {
 let x1
 let y1
 let x2
 let y2
 let rotate
 let nextIndex = trackIndex + 1
 if (coords[nextIndex] === undefined) {
 x1 = coords[nextIndex - 2][0]
 y1 = coords[nextIndex - 2][1]
 x2 = coords[nextIndex - 1][0]
 y2 = coords[nextIndex - 1][1]
 rotate = -Math.atan2(y2 - y1, x2 - x1) + Math.PI
 } else {
 x1 = coords[trackIndex][0]
 y1 = coords[trackIndex][1]
 x2 = coords[nextIndex][0]
 y2 = coords[nextIndex][1]
 rotate = -Math.atan2(y2 - y1, x2 - x1) + Math.PI
 }
 return rotate
 }*/
