package com.br.entity.access;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 角色实体
 * @author zyx
 * @date 2019 02 21
 */

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Role implements Serializable {
    @Getter @Setter private Integer roleSeq;
    @Getter @Setter private String roleName;
    @Getter @Setter private String roleMark;
    @Getter @Setter private Integer roleStatus;
    @Getter @Setter private String roleCreateBy;
    @Getter @Setter private Date roleCreateTime;
    @Getter @Setter private String roleUpdateBy;
    @Getter @Setter private Date roleUpdateTime;
    @Getter @Setter private String roleRemark;
}
