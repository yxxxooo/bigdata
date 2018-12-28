package com.bigdata.bigdata.entity;

import lombok.*;

import java.math.BigDecimal;

@Data
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class UcCert {
    private Long ucCertId;

    private Long ucUserId;

    private Byte certType;

    private String reallyName;

    private String photoPositive;

    private String photoBack;

    private String cardNumber;

    private String remark;

    private BigDecimal createTime;

    private Long maAdminId;

    private Byte certFlag;

    private BigDecimal certTime;

    private String photoLive;
}