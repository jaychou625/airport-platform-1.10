package com.br.queue;

import com.br.constant.MQConstant;
import com.br.entity.log.InterfaceLog;
import com.br.entity.task.WorkCondition;
import com.br.service.log.LogService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * RabbitMQ 消息接收
 *
 * @Author Zero
 * @Date 2019 02 26
 */
public class RMQReceiver {

    // 日志服务
    @Autowired
    private LogService logService;


    @RabbitListener(queues = MQConstant.QUEUE_WORK_CONDITION)
    public void workConditionMessage(WorkCondition workCondition) {

        // ...
    }

    @RabbitListener(queues = MQConstant.QUEUE_WORK_CONDITION)
    public void interfaceLogMessage(InterfaceLog interfaceLog) {
        /*--------------存储日志到数据库-----------------*/
        this.logService.add(interfaceLog);
    }
}
