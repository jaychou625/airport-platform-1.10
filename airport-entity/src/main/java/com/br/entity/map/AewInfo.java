package com.br.entity.map;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

/**
 * 预警信息实体类
 *
 * @Author Zero
 * @Date 2019 04 12
 */

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AewInfo {
    @Getter @Setter Integer aewInfoSeq;
    @Getter @Setter Integer aewSeq;
    @Getter @Setter Integer carSeq;
    @Getter @Setter Integer driverSeq;
    @Getter @Setter String planeSeq;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @Getter @Setter Date aewInfoTime;
    @Getter @Setter Aew aew;
    @Getter @Setter Car car;
    @Getter @Setter Driver driver;
}
