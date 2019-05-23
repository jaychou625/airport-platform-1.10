package com.br.entity.task;

import com.br.entity.map.Car;
import lombok.*;

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
    @Getter @Setter String driverName;
    @Getter @Setter String planeSeq;
    @Getter @Setter Long aewInfoTimestamp;
    @Getter @Setter
    Aew aew;
    @Getter @Setter
    Car car;
}
