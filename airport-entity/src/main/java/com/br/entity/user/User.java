package com.br.entity.user;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户实体
 *
 * @author zyx
 * @date 2019-03-18
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User implements Serializable {
    @Getter @Setter private Integer userSeq;
    @Getter @Setter private String userName;
    @Getter @Setter private String userPwd;
    @Getter @Setter private String userNick;
    @Getter @Setter private Integer userType;
    @Getter @Setter private Integer userSex;
    @Getter @Setter private Integer userStatus;
    @Getter @Setter private String userMobile;
    @Getter @Setter private String userTel;
    @Getter @Setter private String userEmail;
    @Getter @Setter private String userAvatar;
    @Getter @Setter private Date userRegDate;
    @Getter @Setter private Date userLastDate;
    @Getter @Setter private String userLastIp;
    @Getter @Setter private String userUpdateBy;
    @Getter @Setter private Date userUpdateDate;
    @Getter @Setter private Department department;
}