package com.br.mapper;

import com.br.entity.log.InterfaceLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.transaction.annotation.Transactional;

/**
 * 接口日志 Mapper
 *
 * @Author Zero
 * @Date 2019 03 01
 */
@Mapper
@Transactional
public interface InterfaceLogMapper {

    /**
     * 插入预警信息
     * @param interfaceLog 接口日志实体
     * @return Boolean
     */
    boolean add(InterfaceLog interfaceLog);

}
