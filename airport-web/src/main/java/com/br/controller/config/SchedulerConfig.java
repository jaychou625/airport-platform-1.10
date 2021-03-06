package com.br.controller.config;

import com.br.airporttaskserver.handler.TaskApronDataHandler;
import com.br.scheduler.ApronScheduler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 任务调度 配置
 *
 * @Author Zero
 * @Date 2019 02 21
 */
@Configuration
public class SchedulerConfig {

    /**
     * 站坪任务调度
     *
     * @return ApronScheduler
     */
    @Bean
    public ApronScheduler apronScheduler() {
        return new ApronScheduler();
    }

}
