package com.br.service.scheduler;

import com.br.service.service.task.handler.ApronDataHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 站坪任务调度
 */
public class ApronScheduler {

    @Autowired
    private ApronDataHandler apronDataHandler;

    @Scheduled(fixedRate = 2000)
    public void getAndSaveApronData(){
        apronDataHandler.getAndSaveApronData();
    }
}
