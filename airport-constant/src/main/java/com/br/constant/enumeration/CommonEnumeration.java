package com.br.constant.enumeration;

import lombok.*;

/**
 * 常用枚举
 * @Author Zero
 * @Date 2019 03 08
 */
public enum CommonEnumeration {

    SUCCESS(800,"success"), FAILURE(801, "failure");

    @Getter @Setter private String code;
    @Getter @Setter private Integer status;

    CommonEnumeration(Integer status, String code){
        this.status = status;
        this.code = code;
    }

}
