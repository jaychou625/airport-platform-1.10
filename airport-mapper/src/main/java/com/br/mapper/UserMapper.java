package com.br.mapper;

import com.br.entity.core.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;


/**
 * 用户 Mapper
 *
 * @Author zyx
 * @Date 2019 03 01
 */
@Mapper
@Transactional
public interface UserMapper {

    /**
     * 单个用户查询
     * @return
     */
    User find(@Param("userSeq") Integer userSeq, @Param("userName") String userName);
}
