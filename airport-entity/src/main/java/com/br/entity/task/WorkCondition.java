package com.br.entity.task;

import com.br.entity.map.CarInfo;
import com.route.broadcast.PositionNotice;
import lombok.*;

import java.io.Serializable;

/**
 * 工况实体类
 *
 * @Author Zero
 * @Date 2019 05 05
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class WorkCondition implements Serializable {

    @Getter
    @Setter
    private CarInfo carInfo;

    @Getter
    @Setter
    private PositionNotice positionNotice;

}
