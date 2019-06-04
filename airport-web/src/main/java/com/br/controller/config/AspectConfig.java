package com.br.controller.config;

import com.br.log.InterfaceLogAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AOP 配置
 *
 * @Author Zero
 * @Date 2019 02 21
 */
@Configuration
public class AspectConfig {

    /**
     * 接口日志切面
     *
     * @return InterfaceLogAspect
     */
    @Bean
    public InterfaceLogAspect interfaceLogAspect() {
        return new InterfaceLogAspect();
    }
}
