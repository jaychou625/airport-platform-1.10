package com.br.entity.taskState;

import lombok.*;

import java.sql.Timestamp;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EquipmentInfo {

    @Getter @Setter private Integer id;
    @Getter @Setter private String app_Id;
    @Getter @Setter private String account;
    @Getter @Setter private String dev_No;
    @Getter @Setter private String imei;
    @Getter @Setter private String wifi_Mac;
    @Getter @Setter private Date login_Time;
    @Getter @Setter private Date logout_Time;
    @Getter @Setter private Integer status;
    @Getter @Setter private String guid;
    @Getter @Setter private String sim_No;
    @Getter @Setter private String ip_Adress;
    @Getter @Setter private String radio_No;
    @Getter @Setter private Timestamp unixTime;
}
