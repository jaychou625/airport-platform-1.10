package com.br.controller.controller;

import com.br.entity.core.User;
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
     * 获取当前Subject对象
     *
     * @return Subject
     */
    public Subject currentSubject() {
        return SecurityUtils.getSubject();
    }

    /**
     * 获取当前交互用户
     *
     * @return User
     */
    public User currentUser() {
        return (User) this.currentSubject().getPrincipal();
    }

}
