package com.br.entity.websocket;

import lombok.*;

import java.io.Serializable;

/**
 * WebSocket 消息实体
 * @Author Zero
 * @Date 2019 03 08
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class WSMessage implements Serializable {
    @Getter @Setter private String event;
    @Getter @Setter private String message;
}
