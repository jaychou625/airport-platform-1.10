package com.br.entity.map;

import lombok.*;

import java.math.BigDecimal;

/**
 * ADS-B 实体类
 * @Author Zero
 * @Date 2019 03 02
 */

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Plane {
    @Getter @Setter private String planeAddrCode;
    @Getter @Setter private String dataSourceDept;
    @Getter @Setter private BigDecimal planeLongitude;
    @Getter @Setter private BigDecimal planeLatitude;
    @Getter @Setter private Integer planeVerticalSpeed;
    @Getter @Setter private Integer planeGroundVelocity;
    @Getter @Setter private Integer planeHeight;
    @Getter @Setter private String receivePlaneCode;
    @Getter @Setter private String planeSeq;
    @Getter @Setter private BigDecimal planeHeading;
    @Getter @Setter private String ackPlaneCode;
    @Getter @Setter private Long receiveTimeOfLong;
}
