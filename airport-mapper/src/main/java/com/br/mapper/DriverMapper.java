package com.br.mapper;

import com.br.entity.map.Driver;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 司机 Mapper
 *
 * @Author Zero
 * @Date 2019 03 01
 */
@Mapper
public interface DriverMapper {

    /**
     * 获取单个司机
     */
    Driver find(@Param("driverSeq") Integer driverSeq);


}
