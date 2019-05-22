package com.br.entity.map;

import lombok.*;

import java.math.BigDecimal;

/**
 * 车辆信息 实例
 *
 * @Author Zero
 * @Date 2019 03 25
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CarInfo {
    @Getter
    @Setter
    private Integer carInfoSeq;
    @Getter
    @Setter
    private Car car;
    @Getter
    @Setter
    private BigDecimal carAltitude;
    @Getter
    @Setter
    private BigDecimal carLongitude;
    @Getter
    @Setter
    private BigDecimal carLatitude;
    @Getter
    @Setter
    private BigDecimal carBearing;
    @Getter
    @Setter
    private BigDecimal carSpeed;
    @Getter
    @Setter
    private Long receiveTime;
}
