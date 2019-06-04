package com.br.mapper;

import com.br.entity.task.FlightInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.transaction.annotation.Transactional;

/**
 * 航班信息 Mapper
 *
 * @Author Zero
 * @Date 2019 03 01
 */
@Mapper
@Transactional
public interface FlightInfoMapper {


    /**
     * 添加航班信息
     *
     * @return boolean
     */
    boolean add(FlightInfo flightInfo);


}
