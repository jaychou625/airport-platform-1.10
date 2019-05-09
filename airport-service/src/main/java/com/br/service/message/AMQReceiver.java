package com.br.service.message;

import com.br.service.constant.MQConstant;
import com.br.service.service.redis.RedisService;
import com.br.service.service.traffic.PositionService;
import com.br.service.utils.ADSBUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;

/**
 * ActiveMQ 消息接收
 *
 * @Author Zero
 * @Date 2019 02 26
 */
public class AMQReceiver {

    // Redis 服务
    @Autowired
    private RedisService redisService;

    // ABS-B 工具类
    @Autowired
    private ADSBUtils aDSBUtils;

    // 交通工具位置服务
    @Autowired
    private PositionService positionService;

    /**
     * ADS-B 消息接收并且存入 Redis 中
     *
     * @param planes ADS-B 数据集字符串
     */
    @JmsListener(destination = MQConstant.TOPIC_ADSB_RECEIVER)
    public void receiveADSBInfo(String planes) {
        this.positionService.savePlanesInfo(aDSBUtils.toPlaneList(planes));
    }
}
