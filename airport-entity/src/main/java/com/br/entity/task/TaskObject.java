package com.br.entity.task;


import lombok.*;

/**
 * 任务对象 实体类
 * @Author Zero
 * @Date 2019 05 05
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TaskObject {
    @Getter @Setter private Integer taskObjectSeq;
    @Getter @Setter private Long fid;
    @Getter @Setter private String airCorp;
    @Getter @Setter private String fltNo;
    @Getter @Setter private String planeNo;
    @Getter @Setter private String planeMdl;
    @Getter @Setter private String portNo;
    @Getter @Setter private String gate;
    @Getter @Setter private String planDate;
    @Getter @Setter private TaskInfo[] feedBackData;
}
