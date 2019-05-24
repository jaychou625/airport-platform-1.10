package com.br.entity.map;

import lombok.*;

import java.util.Date;

/**
 * 车辆实例
 *
 * @Author Zero
 * @Date 2019 03 25
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Car {
    @Getter
    @Setter
    Integer carSeq;
    @Getter
    @Setter
    String carDept;
    @Getter
    @Setter
    String carSecOfc;
    @Getter
    @Setter
    String carType;
    @Getter
    @Setter
    String carNo;
    @Getter
    @Setter
    String carBrand;
    @Getter
    @Setter
    String carName;
    @Getter
    @Setter
    Date carObtainDate;
    @Getter
    @Setter
    Date carStartDate;
    @Getter
    @Setter
    String carFuelType;
    @Getter
    @Setter
    String carBasicPar;
    @Getter
    @Setter
    String carValue;
    @Getter
    @Setter
    private String deviceIMEI;
    @Getter
    @Setter
    String carSerCdt;

}
