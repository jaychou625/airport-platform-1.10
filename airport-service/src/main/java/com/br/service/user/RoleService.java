package com.br.service.user;

import com.br.entity.access.Role;
import com.br.mapper.RoleMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色服务
 *
 * @Author zyx
 * @Date 2019 03 31
 */
@Service("roleService")
public class RoleService {

    // 角色 Mapper
    @Resource
    private RoleMapper roleMapper;

    /**
     * 获取所有角色 By 用户编号
     *
     * @param userSeq
     * @return List<Role>
     */
    public List<Role> findRolesByUserSeq(Integer userSeq) {
        return this.roleMapper.findRolesByUserSeq(userSeq);
    }
}
