package com.br.mapper;

import com.br.entity.task.TaskInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 任务信息 Mapper
 *
 * @Author Zero
 * @Date 2019 03 01
 */
@Mapper
public interface TaskInfoMapper {

    /**
     * 添加任务信息
     * @param taskInfo 任务信息
     * @return boolean
     */
    boolean add(TaskInfo taskInfo);


}
