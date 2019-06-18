package com.br.mapper;

import com.br.entity.taskState.EquipmentInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

@Mapper
@Transactional
public interface EquipmentInfoMapper {
    /**
     * 添加任务设备信息
     * @param equipmentInfo 任务信息
     * @return boolean
     */
    boolean add(EquipmentInfo equipmentInfo);

    /**
     * 根据id查询数据库有无重复数据
     * @param id
     * @return
     */
    int selectById(@Param("id") Integer id);
}
