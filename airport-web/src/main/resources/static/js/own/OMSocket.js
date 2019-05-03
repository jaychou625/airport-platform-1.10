/*------------------------- 服务器IP与端口 ----------------------------*/
function webSocketInitialization() {
    if ("WebSocket" in window) {
        websocket = new WebSocket("ws://" + serverIp + ":" + serverPort + "/Wsocket")
        /*------------------------- websocket 打开事件  ----------------------------*/
        websocket.onopen = function () {
            console.log("websocket build success.")
        }
        /*------------------------- websocket 关闭事件  ----------------------------*/
        websocket.onclose = function () {
            websocket.close()
            console.log("websocket close.")
        }

        /*------------------------- websocket 消息事件  ----------------------------*/
        websocket.onmessage = function (event) {
            let message = messageFormat(event.data)
            switch (message.event) {
                case wsEvents.EVENT_PLANE_POSITION: // 飞机位置事件
                    buildOrFlushPlanePositionObject(message.data, vectorLayer.getSource())
                    break
                case wsEvents.EVENT_CAR_POSITION: // 车辆位置事件
                    buildOrFlushTrafficPositionObject(message.data, vectorLayer.getSource())
                    break
                case wsEvents.EVENT_ROUTE_PLAN: // 路径规划事件
                    routePlan(message.data, vectorLayer.getSource())
                    break
                case wsEvents.EVENT_ROUTE_CROSS: // 冲突点事件
                    routeCross(message.data, vectorLayer.getSource());
                    break
                case wsEvents.EVENT_CLEAN: // 清除事件
                    removeRoute(vectorLayer.getSource(), message.data)
                    removeCross(vectorLayer.getSource(), message.data)
                    break
                default:
                    console.log("no other event")
            }
        }
    } else {
        alert("sorry, your browser version is too low, please update to the latest version to use again")
        return;
    }
}

// 消息数据格式化
function messageFormat(message) {
    message = JSON.parse(message)
    return {event: message.event, data: JSON.parse(message.message)}
}