package com.br.utils;

import com.br.entity.core.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * Shiro Subject 工具类
 */
public class SubjectUtils {

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
