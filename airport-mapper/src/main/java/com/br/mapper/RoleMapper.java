package com.br.mapper;

import com.br.entity.access.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 角色 Mapper
 *
 * @Author zyx
 * @Date 2019 03 01
 */
@Mapper
@Transactional
public interface RoleMapper {

    /**
     * 查询角色集合 By 用户序号
     *
     * @param userSeq 用户序号
     * @return List<Role>
     */
    List<Role> findRolesByUserSeq(@Param("userSeq") Integer userSeq);
}
