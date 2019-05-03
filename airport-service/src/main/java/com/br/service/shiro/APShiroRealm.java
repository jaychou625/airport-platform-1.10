package com.br.service.shiro;

import com.br.entity.access.Menu;
import com.br.entity.access.Role;
import com.br.entity.core.User;
import com.br.service.service.user.MenuService;
import com.br.service.service.user.RoleService;
import com.br.service.service.user.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Shiro 域
 *
 * @Author zyx
 * @Date 2019 02 21
 */
public class APShiroRealm extends AuthorizingRealm {

    // 用户服务
    @Autowired
    private UserService userService;

    // 角色服务
    @Autowired
    private RoleService roleService;

    // 菜单服务
    @Autowired
    private MenuService menuService;


    public APShiroRealm() {
        this.setName("APShiroRealm");
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection collection) {
        User user = (User) collection.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        List<Integer> rolesSeq = new ArrayList<>();
        for (Role role : this.roleService.findRolesByUserSeq(user.getUserSeq())) {
            rolesSeq.add(role.getRoleSeq());
            authorizationInfo.addRole(role.getRoleMark());
        }
        for (Menu menu : this.menuService.getMenusByRoleSeq(rolesSeq)) {
            authorizationInfo.addStringPermission(menu.getMenuUrl());
        }
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String userName = upToken.getUsername();
        User user = this.userService.find(null, userName);
        if(user == null) {
            throw new UnknownAccountException();
        }
        return new SimpleAuthenticationInfo(user, user.getUserPwd(), this.getName());
    }

}
