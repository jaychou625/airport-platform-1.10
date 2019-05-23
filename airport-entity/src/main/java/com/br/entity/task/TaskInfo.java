package com.br.entity.task;

import lombok.*;

import java.util.Date;

/**
 * 任务信息实体类
 * @Author Zero
 * @Date 2019 05 05
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TaskInfo {
    @Getter @Setter private Integer taskInfoSeq;
    @Getter @Setter private Long fid;
    @Getter @Setter private Date sendTime;
    @Getter @Setter private String prcName;
    @Getter @Setter private String sendUserName;
    @Getter @Setter private String sendOrganName;
    @Getter @Setter private Date assignTime;
    @Getter @Setter private String feedBackName;
    @Getter @Setter private String feedBackOrganName;
    @Getter @Setter private Date acceptTime;
    @Getter @Setter private Date placeTime;
    @Getter @Setter private Date startTime;
    @Getter @Setter private Date endTime;
    @Getter @Setter private Integer feedBackType;
    //计数，判断是否为第一次执行任务，0为第一次
    @Getter @Setter private Integer count;
    //任务编号，自定义
    @Getter @Setter private String taskNo;
    //车牌号，自定义
    @Getter @Setter private String carNo;
}
