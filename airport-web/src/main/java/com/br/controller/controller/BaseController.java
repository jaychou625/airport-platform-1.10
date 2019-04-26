package com.br.controller.controller;

import com.br.entity.user.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * 基础控制器
 *
 * @Author Zero
 * @Date 2019 02 21
 */
public class BaseController {

    /**
     * 获取当前交互用户
     *
     * @return
     */
    public User currentUser() {
        Subject subject = SecurityUtils.getSubject();
        return (User) subject.getPrincipal();
    }

}
