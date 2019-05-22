package com.br.controller.config;

import com.br.service.message.AMQReceiver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 消息中间件 配置
 *
 * @Author Zero
 * @Date 2019 02 21
 */
@Configuration
public class MQConfig {

    /**
     * AMQ消息接收
     *
     * @return AMQReceiver
     */
    @Bean
    public AMQReceiver amqReceiver() {
        return new AMQReceiver();
    }

    /**
     * XYX消息接收
     *
     * @return XYXReceiver
     */
   /* @Bean
    public XYXReceiver xyxReceiver() {
        return new XYXReceiver();
    }*/

}
