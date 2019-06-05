package com.br.controller.config;

import com.br.constant.MQConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
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
   /* @Bean
    public AMQReceiver amqReceiver() {
        return new AMQReceiver();
    }
*/
    /**
     * XYX消息接收
     *
     * @return XYXReceiver
     */
    /*@Bean
    public XYXReceiver xyxReceiver() {
        return new XYXReceiver();
    }*/


    /**
     * 队列: 工况结果
     */
    @Bean
    public Queue workConditionQueue() {
        return new Queue(MQConstant.QUEUE_WORK_CONDITION);
    }

    /**
     * 队列: 接口日志
     */
    @Bean
    public Queue interfaceLogQueue() {
        return new Queue(MQConstant.QUEUE_INTERFACE_LOG);
    }


    /**
     * 主题交换机
     */
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(MQConstant.EXCHANGE_TOPIC);
    }

    /**
     * 绑定工况消息到交换机
     */
    @Bean
    public Binding bindingWorkConditionMessageToExchange() {
        return BindingBuilder.bind(workConditionQueue()).to(topicExchange()).with(MQConstant.TOPIC_WORK_CONDITION);
    }

    /**
     * 绑定工况消息到交换机
     */
    @Bean
    public Binding bindingInterfaceLogMessageToExchange() {
        return BindingBuilder.bind(interfaceLogQueue()).to(topicExchange()).with(MQConstant.TOPIC_INTERFACE_LOG);
    }

}
