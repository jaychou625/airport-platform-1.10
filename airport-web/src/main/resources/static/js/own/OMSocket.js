// 初始化websocket
function webSocketInitialization() {
    if ("WebSocket" in window) {
        websocket = new WebSocket("ws://localhost:9090/Wsocket")
        // websocket 当连接建立时触发
        websocket.onopen = function () {
            console.log("websocket build success.")
        }

        // websocket 当连接关闭时触发
        websocket.onclose = function () {
            closeWebSocket()
            console.log("websocket close.")
        }

        // websocket 当接收到消息时触发
        websocket.onmessage = function (event) {
            let message = messageFormat(event.data)

            switch (message.event) {
                case "EVENT_PLANE_POSITION": // 飞机位置事件
                    buildOrFlushPlanePositionObject(message.data, vectorLayer.getSource())
                    break
                case "EVENT_CAR_POSITION": // 车辆位置事件
                    buildOrFlushTrafficPositionObject(message.data, vectorLayer.getSource())
                    break
                case "EVENT_ROUTE_PLAN": // 路径规划事件
                    routePlan(message.data, vectorLayer.getSource())
                    break
                case "EVENT_RE_ROUTE_PLAN": // 路径重规划事件
                    removeRoute(vectorLayer.getSource(), message.data)
                    break
                case "EVENT_ROUTE_CROSS": // 冲突点事件
                    routeCross(message.data);
                    break
                case "EVENT_ARRIVE_TARGET": // 删除规划事件与冲突点
                    removeRoute(vectorLayer.getSource(), message.data)
                    removeCross(vectorLayer.getSource(), message.data)
                    break
                default:
                    console.log("default")
            }
            /*if (!vectorLayerFlag) {

                vectorLayerFlag = true
            }*/
        }
    } else {
        alert("sorry, your browser version is too low, please update to the latest version to use again")
        return;
    }
}


// 关闭websocket连接
function closeWebSocket() {
    websocket.close()
}

// 消息数据格式化
function messageFormat(message) {
    message = JSON.parse(message)
    return {event: message.event, data: JSON.parse(message.message)}
}