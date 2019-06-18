package com.br.mapper;

import com.br.entity.taskState.EquipmentInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

@Mapper
@Transactional
public interface TaskAccountMapper {
    /**
     * 根据account查询司机姓名
     * @param account
     * @return
     */
    String selectByAccount(@Param("account") String account);
}
