package com.br.controller.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * WebSocket 配置
 * @Author Zero
 * @Date 2019 02 21
 */
@Configuration
public class WebSocketConfig {
    /**
     * WebSocket Engine Scan 实例
     *
     * @return ServerEndpointExporter 服务端点出口
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
