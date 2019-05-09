package com.br.mapper;

import com.br.entity.task.AewInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预警信息 Mapper
 *
 * @Author Zero
 * @Date 2019 03 01
 */
@Mapper
public interface AewInfoMapper {


    /**
     * 获取所有预警信息
     *
     * @return List
     */
    List<AewInfo> findAll();


    /**
     * 获取单个预警信息
     */
    AewInfo find(@Param("aewInfoSeq") Integer aewInfoSeq);

    /**
     * 插入预警信息
     */
    boolean add(AewInfo aewInfo);


}
