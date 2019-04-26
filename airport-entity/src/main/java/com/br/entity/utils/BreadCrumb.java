package com.br.entity.utils;

import lombok.*;

/**
 * 面包屑实例
 * @Author Zero
 * @Date 2019 03 25
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BreadCrumb {
    @Getter @Setter String urlName;
    @Getter @Setter String url;
}
