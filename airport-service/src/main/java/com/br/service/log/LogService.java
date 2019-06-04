package com.br.service.log;

import com.br.entity.log.InterfaceLog;
import com.br.mapper.InterfaceLogMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 日志服务
 *
 * @Author Zero
 * @Date 2019 02 22
 */

@Service("logService")
public class LogService {


    // 接口日志 mapper
    @Resource
    private InterfaceLogMapper interfaceLogMapper;


    /**
     * 插入接口日志信息
     *
     * @param interfaceLog 接口日志实例
     */
    public void add(InterfaceLog interfaceLog) {
        this.interfaceLogMapper.add(interfaceLog);
    }


}
