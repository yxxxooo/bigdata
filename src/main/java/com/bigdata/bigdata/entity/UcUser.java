package com.bigdata.bigdata.entity;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class UcUser {
    /**
     * 用户编号
     * <p>
     * uc_user.uc_user_id
     */
    private Long ucUserId;

    /**
     * 语言编号
     * <p>
     * uc_user.ba_lang_id
     */
    private Long baLangId;

    /**
     * 国家编号
     * <p>
     * uc_user.ba_national_id
     */
    private Long baNationalId;

    /**
     * 用户类型
     * <p>
     * uc_user.source_type
     */
    private Integer sourceType;
    
    /**
     * 注册类型
     * <p>
     * uc_user.logon_mode
     */
    private Integer logonMode;

    /**
     * 环信编号
     * <p>
     * uc_user.uc_ring_letter_id
     */
    private Long ucRingLetterId;

    /**
     * IDD代码,国家直拨区号
     * <p>
     * uc_user.idd_code
     */
    private String iddCode;

    /**
     * 注册手机号码
     * <p>
     * uc_user.mobile
     */
    private String mobile;

    /**
     * 登录密码
     * <p>
     * uc_user.login_passwd
     */
    private String loginPasswd;

    /**
     * 登录salt
     * <p>
     * uc_user.login_salt
     */
    private String loginSalt;

    /**
     * 支付密码
     * <p>
     * uc_user.pay_passwd
     */
    private String payPasswd;

    /**
     * 支付salt
     * <p>
     * uc_user.pay_salt
     */
    private String paySalt;

    /**
     * 登录次数，每天更新
     * <p>
     * uc_user.login_times
     */
    private Integer loginTimes;

    /**
     * 尝试次数，登录尝试次数
     * <p>
     * uc_user.error_times
     */
    private Integer errorTimes;

    /**
     * 注册时间
     * <p>
     * uc_user.create_time
     */
    private BigDecimal createTime;

    /**
     * uc_user.lastlogin_time
     */
    private BigDecimal lastloginTime;

    /**
     * 锁定时间
     * <p>
     * uc_user.lock_time
     */
    private BigDecimal lockTime;

    /**
     * 0添加好友，1邀请注册，2扫码注册
     * <p>
     * uc_user.reg_method
     */
    private Short regMethod;

    /**
     * 注册邀请码
     * <p>
     * uc_user.req_code
     */
    private String reqCode;

    /**
     * 用户注册时IP
     * <p>
     * uc_user.reg_ip
     */
    private String regIp;

    /**
     * 用户是否删除，1:正常，2:删除 3:冻结 4:注销
     * <p>
     * uc_user.state
     */
    private Integer state;

    /**
     * 设备信息，json, 包含IP地址
     * <p>
     * uc_user.device_info
     */
    private String deviceInfo;

    // ------------------------------

    /**
     * 修改登录密码时间戳
     */
    private Date modifyPasswardTime;
}