package com.br.airportequipmentcommunication.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GPSInfo {
    @Getter
    @Setter
    private String id;
    @Getter
    @Setter
    private Double latitude;
    @Getter
    @Setter
    private String latitudeDirection;
    @Getter
    @Setter
    private Double longitude;
    @Getter
    @Setter
    private String longitudeDirection;
    @Getter
    @Setter
    private Double courseAngle;
    @Getter
    @Setter
    private Double speed;
    @Getter
    @Setter
    private Double magneticDeclination;
    @Getter
    @Setter
    private String magneticDeclinationDirection;
}
