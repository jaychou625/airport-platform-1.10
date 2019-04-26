package com.br.service.service.websocket;

import com.br.service.websocket.WSocket;
import org.springframework.stereotype.Service;

/**
 * WebSocket 服务
 * @Author Zero
 * @Date 2019 02 22
 */
@Service("wsService")
public class WSService {

    /**
     * WebSocket 发送消息
     * @param message 消息
     */
    public void wsSend(String message) {
        WSocket.sends(message);
    }

    /**
     * WebSocket 发送消息
     * @param message 对象
     */
    public void wsSend(Object message) {
        WSocket.sends(message);
    }

}
