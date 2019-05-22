package com.br.entity.task;

import lombok.*;

import java.util.Date;

/**
 * 任务状态信息实体类
 * @Author Zero
 * @Date 2019 05 05
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TaskStateInfo {
    //唯一标示，任务编号
    @Getter
    @Setter
    private String id;
    //任务车辆类型
    @Getter
    @Setter
    private String carType;
    //任务航班号
    @Getter
    @Setter
    private String fltNo;
    //任务车辆车牌号
    @Getter
    @Setter
    private String carNo;
    //接单司机
    @Getter
    @Setter
    private String driverName;
    //任务开始时间
    @Getter
    @Setter
    private Date startTime;
    //任务结束时间
    @Getter
    @Setter
    private Date endTime;
    //状态
    @Getter
    @Setter
    private Integer state;
    //机位信息
    @Getter
    @Setter
    private String portNo;

}
