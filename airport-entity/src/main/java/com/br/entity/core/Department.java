package com.br.entity.core;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 部门实体
 *
 * @author zyx
 * @date 2019-03-18
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Department implements Serializable {
    @Getter @Setter private Integer deptSeq;
    @Getter @Setter private String deptName;
    @Getter @Setter private Integer deptStatus;
    @Getter @Setter private String deptCreateBy;
    @Getter @Setter private Date deptCreateTime;
    @Getter @Setter private String deptUpdateBy;
    @Getter @Setter private Date deptUpdateTime;
    @Getter @Setter private Department deptParentDepartment;
}
