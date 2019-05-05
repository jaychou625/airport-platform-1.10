package com.br.entity.task;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 任务实例
 * @Author Zero
 * @Date 2019 04 08
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TrafficTask implements Serializable {
    @Getter @Setter String taskEvent;
    @Getter @Setter String carSeq;
    @Getter @Setter String planeSeq;
    @Getter @Setter String targetOfPointName;
    @Getter @Setter BigDecimal[] targetOfPoint;
}

