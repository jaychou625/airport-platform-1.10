package com.br.constant.enumeration;
import lombok.*;
/**
 * Shiro 枚举
 *
 * @Author Zero
 * @Date 2019 03 08
 */
public enum ShiroEnumeration {

    USERNAME_OR_PWD_ERROR(701, "IncorrectCredentialsException", "密码错误"),
    UNKNOWN_USER(702, "UnknownAccountException", "用户不存在"),
    LOGIN_SUCCESS(703, "LOGIN SUCCESS", "登录成功");

    @Getter
    @Setter
    private Integer status;
    @Getter
    @Setter
    private String code;
    @Getter
    @Setter
    private String message;


    ShiroEnumeration(Integer status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

}
