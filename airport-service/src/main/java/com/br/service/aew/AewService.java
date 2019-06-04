package com.br.service.aew;

import com.br.entity.task.AewInfo;
import com.br.mapper.AewInfoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 预警服务
 *
 * @Author Zero
 * @Date 2019 03 21
 */

@Service("aewService")
public class AewService {

    // Aew Mapper
    @Resource
    private AewInfoMapper aewInfoMapper;


    /**
     * 获取所有预警信息
     *
     * @return List<AewInfo>
     */
    public List<AewInfo> findAll() {
        return this.aewInfoMapper.findAll();
    }

    /**
     * 插入预警信息
     *
     * @param aewInfo 预警信息实例
     */
    public void add(AewInfo aewInfo) {
        this.aewInfoMapper.add(aewInfo);
    }


}