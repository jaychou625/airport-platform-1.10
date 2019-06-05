package com.br.constant;

/**
 * MQ 常量
 *
 * @Author Zero
 * @Date 2019 02 26
 */
public class MQConstant {
    // ADSB消息订阅主题
    public final static String TOPIC_ADSB_RECEIVER = "ADSB.Receiver";
    // 新运行QCU
    public final static String QCU_XYX = "qcu1";
    // 新运行队列
    public final static String QUEUE_XYX = "que_4gtogarims";
    // RMQ 工况结果队列
    public final static String QUEUE_WORK_CONDITION = "TOPIC_WORK_CONDITION";
    // RMQ 接口日志队列
    public final static String QUEUE_INTERFACE_LOG = "TOPIC_INTERFACE_LOG";
    // RMQ 工况结果routingKey
    public final static String TOPIC_WORK_CONDITION = "TOPIC_WORK_CONDITION";
    // RMQ 接口日志routingKey
    public final static String TOPIC_INTERFACE_LOG = "TOPIC_INTERFACE_LOG";
    // RMQ 主题交换机
    public final static String EXCHANGE_TOPIC = "EXCHANGE_TOPIC";
}
