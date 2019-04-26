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
    @Getter @Setter private String aircraftSeq;
    @Getter @Setter private String dataSourceDept;
    @Getter @Setter private BigDecimal aircraftLongitude;
    @Getter @Setter private BigDecimal aircraftLatitude;
    @Getter @Setter private Integer aircraftVerticalSpeed;
    @Getter @Setter private Integer aircraftGroundVelocity;
    @Getter @Setter private Integer aircraftHeight;
    @Getter @Setter private String receiveAircraftCode;
    @Getter @Setter private String flightNumber;
    @Getter @Setter private BigDecimal aircraftHeading;
    @Getter @Setter private String ackAircraftCode;
    @Getter @Setter private Long receiveTimeOfLong;
}
