package com.br.entity.utils;

import lombok.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 响应结果实体
 * @Author Zero
 * @Date 2019 02 22
 */

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Result implements Serializable {
    @Getter @Setter Integer status;
    @Getter @Setter String code;
    @Getter @Setter Map<String, Object> data = new HashMap<>();
}
