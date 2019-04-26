package com.br.mapper;

import com.br.entity.map.Car;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 车辆 Mapper
 *
 * @Author Zero
 * @Date 2019 03 01
 */
@Mapper
public interface CarMapper {


    /**
     * 获取所有车辆信息
     *
     * @return List
     */
    List<Car> findAll();

    /**
     * 获取单个车辆信息
     *
     * @param carSeq 车辆序号
     * @return
     */
    Car find(@Param("carSeq") Integer carSeq);


}
