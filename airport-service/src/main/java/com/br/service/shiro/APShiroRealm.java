package com.br.service.shiro;

import com.br.entity.access.Menu;
import com.br.entity.access.Role;
import com.br.entity.user.User;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        List<Role> roles_temp = new ArrayList<>();
        Set<String> roles = new HashSet<>();
        Set<String> menus = new HashSet<>();
        for (Role role : this.roleService.findRolesByUserSeq(user.getUserSeq())) {
            roles.add(role.getRoleMark());
            roles_temp.add(role);
        }
        for (Menu menu : this.menuService.getMenusByRoleSeq(roles_temp)) {
            menus.add(menu.getMenuUrl());
        }
        authorizationInfo.setRoles(roles);
        authorizationInfo.setStringPermissions(menus);
        System.out.println("enter");
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
        return new SimpleAuthenticationInfo(user, user.getUserPwd(), this.getClass().getName());
    }

}
