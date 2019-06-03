package com.br.websocket.server;

import com.br.websocket.encoder.WSMessageEncoder;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * WebSocket 服务端
 *
 * @Author Zero
 * @Date 2019 02 22
 */
@ServerEndpoint(value = "/Wsocket", encoders = {WSMessageEncoder.class})
@Component
public class WSocket {

    // 客户端集合
    private static Map<String, Session> clients = clientsInit();

    // 当前客户端Session
    private Session session;

    /**
     * 初始化客户端集合
     *
     * @return Map
     */
    private static Map<String, Session> clientsInit() {
        Map<String, Session> map = new HashMap<>();
        Collections.synchronizedMap(map);
        return map;
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        clients.put(session.getId(), session);
    }

    @OnClose
    public void onClose() {
        clients.remove(this.session.getId());
    }

    @OnError
    public void onError(Session session, Throwable error) {
    }

    /**
     * WebSocket 群发消息
     *
     * @param message 对象
     */
    public static void sends(Object message) {
        for (String sessionId : clients.keySet()) {
            clients.get(sessionId).getAsyncRemote().sendObject(message);
        }
    }
}
