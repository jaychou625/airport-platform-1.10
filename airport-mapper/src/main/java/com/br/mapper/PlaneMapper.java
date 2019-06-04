package com.br.mapper;

import com.br.entity.map.Plane;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.transaction.annotation.Transactional;

/**
 * 飞机 Mapper
 *
 * @Author Zero
 * @Date 2019 03 01
 */
@Mapper
@Transactional
public interface PlaneMapper {


    /**
     * 添加飞机信息
     * @param plane 飞机信息
     * @return boolean
     */
    boolean add(Plane plane);


}
