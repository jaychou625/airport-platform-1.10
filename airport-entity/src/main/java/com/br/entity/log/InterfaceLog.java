package com.br.entity.log;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 接口日志实体
 *
 * @Author Zero
 * @date 2019-06-04
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InterfaceLog implements Serializable {
    @Getter
    @Setter
    private Integer interfaceLogSeq;
    @Getter
    @Setter
    private String interfaceLogTier;
    @Getter
    @Setter
    private String interfaceLogUserName;
    @Getter
    @Setter
    private String interfaceLogBizOperation;
    @Getter
    @Setter
    private Long interfaceLogTimestamp;
    @Getter
    @Setter
    private Date interfaceLogTime;
    @Getter
    @Setter
    private String interfaceLogClassName;
    @Getter
    @Setter
    private String interfaceLogMethodName;
    @Getter
    @Setter
    private String interfaceLogArgs;
    @Getter
    @Setter
    private String interfaceLogRequestIp;
    @Getter
    @Setter
    private String interfaceLogRequestUrl;

}
