package com.br.entity.map;

import lombok.*;

/**
 * 司机实体类
 *
 * @Author Zero
 * @Date 2019 04 13
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Driver {
    @Getter
    @Setter
    Integer driverSeq;
    @Getter
    @Setter
    String driverName;
}
