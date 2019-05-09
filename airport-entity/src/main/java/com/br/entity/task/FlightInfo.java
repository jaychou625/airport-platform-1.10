package com.br.entity.task;

import lombok.*;

/**
 * 航班状态 实体类
 * @Author Zero
 * @Date 2019 03 02
 */

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FlightInfo {
    @Getter @Setter private Integer flightInfoSeq;
    @Getter @Setter private String bizType;
    @Getter @Setter private String dataTime;
    @Getter @Setter private String method;
    @Getter @Setter private String type;
    @Getter @Setter private String flightAttrSeq;
    @Getter @Setter private String flightAttr;
    @Getter @Setter private String flightTypeSeq;
    @Getter @Setter private String flightType;
    @Getter @Setter private String aircraftType;
    @Getter @Setter private String registration;
    @Getter @Setter private String flightNumber;
    @Getter @Setter private String primaryFlt;
    @Getter @Setter private String relatedFlt;
    @Getter @Setter private String flightStatusSeq;
    @Getter @Setter private String flightStatus;
    @Getter @Setter private String portNo;
    @Getter @Setter private String planDate;
    @Getter @Setter private String adt;
    @Getter @Setter private String edt;
    @Getter @Setter private String sdt;
    @Getter @Setter private String arrSdt;
    @Getter @Setter private String cobt;
    @Getter @Setter private String origin;
    @Getter @Setter private String destination;
    @Getter @Setter private String via;
    @Getter @Setter private String counterOpenDT;
    @Getter @Setter private String counter;
    @Getter @Setter private String counterInt;
    @Getter @Setter private String depTerminal;
    @Getter @Setter private String gate;
    @Getter @Setter private String gateInt;
    @Getter @Setter private String belt;
    @Getter @Setter private String beltInt;
    @Getter @Setter private Integer psgTotal;
    @Getter @Setter private Integer bagNum;
    @Getter @Setter private Integer bagWeight;
    @Getter @Setter private String arrFlightNumber;
    @Getter @Setter private String arrFltType;
    @Getter @Setter private String depFlightNumber;
    @Getter @Setter private String depFltType;
}
