package com.br.scheduler;

import com.br.service.task.handler.ApronDataHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 站坪任务调度
 */
public class ApronScheduler {

    // 站坪任务数据处理
    @Autowired
    private ApronDataHandler apronDataHandler;

    /**
     * 任务调度: 获取站坪任务信息 (2s/次)
     */
    @Scheduled(fixedRate = 2000)
    public void getAndSaveApronData(){
        apronDataHandler.getAndSaveApronData();
    }
}
