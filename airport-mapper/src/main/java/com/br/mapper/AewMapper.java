package com.br.mapper;

import com.br.entity.map.Aew;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 预警 Mapper
 *
 * @Author Zero
 * @Date 2019 03 01
 */
@Mapper
public interface AewMapper {

    /**
     * 获取单个预警
     */
    Aew find(@Param("aewSeq") Integer aewSeq);


}
