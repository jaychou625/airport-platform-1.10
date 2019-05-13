package com.br.mapper;

import com.br.entity.task.TaskObject;
import org.apache.ibatis.annotations.Mapper;

/**
 * 任务对象 Mapper
 *
 * @Author Zero
 * @Date 2019 03 01
 */
@Mapper
public interface TaskObjectMapper {

    /**
     * 添加任务对象
     * @param taskObject 任务对象
     * @return boolean
     */
    boolean add(TaskObject taskObject);


}
