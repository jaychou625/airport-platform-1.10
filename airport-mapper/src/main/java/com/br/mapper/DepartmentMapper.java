package com.br.mapper;

import com.br.entity.core.Department;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;


/**
 * 部门 Mapper
 *
 * @Author zyx
 * @Date 2019 03 01
 */
@Mapper
@Transactional
public interface DepartmentMapper {

    /**
     * 单个部门查询 By 序号
     * @return
     */
    Department find(@Param("deptSeq") Integer deptSeq);

}
