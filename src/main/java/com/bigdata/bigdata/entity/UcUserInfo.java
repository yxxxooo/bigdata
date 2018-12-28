package com.bigdata.bigdata.entity;

import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class UcUserInfo {
    /**
     *   用户编号
     *
     *   uc_user_info.uc_user_id
     */
    private Long ucUserId;

    /**
     *   用户名
     *
     *   uc_user_info.user_name
     */
    private String userName;

    /**
     *   昵称
     *
     *   uc_user_info.nick
     */
    private String nick;

    /**
     *   头像文件
     *
     *   uc_user_info.symbol
     */
    private String symbol;

    /**
     *   角色编号
     *
     *   uc_user_info.role_id
     */
    private String roleId;

    /**
     *   实名认证标记 0未认证 1已认证
     *
     *   uc_user_info.cert_flag
     */
    private Byte certFlag;

    /**
     *   电话号码
     *
     *   uc_user_info.telephone
     */
    private String telephone;

    /**
     *   性别
     *
     *   uc_user_info.sex
     */
    private String sex;

    /**
     *   详细地址
     *
     *   uc_user_info.address
     */
    private String address;

    /**
     *
     *   uc_user_info.first_name
     */
    private String firstName;

    /**
     *
     *   uc_user_info.middle_name
     */
    private String middleName;

    /**
     *
     *   uc_user_info.last_name
     */
    private String lastName;

    /**
     *   省
     *
     *   uc_user_info.province
     */
    private String province;

    /**
     *   市
     *
     *   uc_user_info.city
     */
    private String city;

    /**
     *   区县
     *
     *   uc_user_info.district
     */
    private String district;

    private Date birthday;

    /**
     *   城镇
     *
     *   uc_user_info.towns
     */
    private String towns;

    /**
     *   电子邮件
     *
     *   uc_user_info.email
     */
    private String email;
}