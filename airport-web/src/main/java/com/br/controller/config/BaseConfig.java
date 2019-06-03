package com.br.controller.config;

import com.br.utils.ADSBUtils;
import com.br.utils.CrypUtils;
import com.br.utils.DateUtils;
import com.br.utils.FileUtils;
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
     * @return FileUtils
     */
    @Bean
    public FileUtils fileUtils() {
        return new FileUtils();
    }

    /**
     * ADS-B工具类
     *
     * @return ADSBUtils
     */
    @Bean
    public ADSBUtils adsbUtils() {
        return new ADSBUtils();
    }

    /**
     * 密码工具类
     *
     * @return CrypUtils
     */
    @Bean
    public CrypUtils crypUtils() {
        return new CrypUtils();
    }

    /**
     * 时间工具类
     *
     * @return CrypUtils
     */
    @Bean
    public DateUtils dateUtils() {
        return new DateUtils();
    }



}
