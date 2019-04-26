package com.br.controller.config;

import com.br.service.utils.FileUtils;
import com.route.broadcast.RealNavServer;
import com.route.imp.NavEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Map 配置
 *
 * @Author Zero
 * @Date 2019 02 21
 */
@Configuration
public class MapConfig {

    /**
     * 导航引擎
     *
     * @return NavEngine
     */
    @Bean
    public NavEngine navEngine() {
        return new NavEngine();
    }

    /**
     * 读取地图文件
     *
     * @return byte[]
     */
    @Bean
    public byte[] readMapToByte(FileUtils fileUtils) {
        return fileUtils.readFile("static/map-resources/4602000001/E_0");
    }

    /**
     * 预警服务实例
     *
     * @param fileUtils 文件工具类
     * @return
     */
    @Bean
    public RealNavServer realNavServer(FileUtils fileUtils) {
        return new RealNavServer(readMapToByte(fileUtils));
    }
}
