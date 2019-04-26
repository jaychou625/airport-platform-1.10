package com.br.service.websocket;

import com.alibaba.fastjson.JSON;
import com.br.entity.websocket.WSMessage;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * WebSocket 编码
 * @Author Zero
 * @Date 2019 03 08
 */
public class WSMessageEncoder implements Encoder.Text<WSMessage>{

    @Override
    public String encode(WSMessage wsMessage) {
        return JSON.toJSONString(wsMessage);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {}

    @Override
    public void destroy() {}

}
