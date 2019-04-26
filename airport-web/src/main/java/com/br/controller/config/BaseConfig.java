package com.br.controller.config;

import com.br.service.utils.ADSBUtils;
import com.br.service.utils.FileUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 基础 配置
 *
 * @Author Zero
 * @Date 2019 02 21
 */
@Configuration
public class BaseConfig {

    /**
     * 文件工具类
     *
     * @return
     */
    @Bean
    public FileUtils fileUtils() {
        return new FileUtils();
    }

    /**
     * ADS-B工具类
     *
     * @return
     */
    @Bean
    public ADSBUtils adsbUtils() {
        return new ADSBUtils();
    }


}
