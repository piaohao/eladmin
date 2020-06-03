/*
*  Copyright 2019-2020 Zheng Jie
*
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*
*  http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*  See the License for the specific language governing permissions and
*  limitations under the License.
*/
package me.zhengjie.api.domain.biz;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

/**
* @website https://el-admin.vip
* @description /
* @author piaohao
* @date 2020-06-03
**/
@Entity
@Data
@Table(name="biz_company_user")
public class CompanyUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    @Column(name = "provider",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "provider")
    private String provider;

    @Column(name = "uid",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "uid")
    private String uid;

    @Column(name = "encrypted_password",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "encryptedPassword")
    private String encryptedPassword;

    @Column(name = "reset_password_token")
    @ApiModelProperty(value = "resetPasswordToken")
    private String resetPasswordToken;

    @Column(name = "reset_password_sent_at")
    @ApiModelProperty(value = "resetPasswordSentAt")
    private Timestamp resetPasswordSentAt;

    @Column(name = "allow_password_change")
    @ApiModelProperty(value = "allowPasswordChange")
    private Integer allowPasswordChange;

    @Column(name = "remember_created_at")
    @ApiModelProperty(value = "rememberCreatedAt")
    private Timestamp rememberCreatedAt;

    @Column(name = "confirmation_token")
    @ApiModelProperty(value = "confirmationToken")
    private String confirmationToken;

    @Column(name = "confirmed_at")
    @ApiModelProperty(value = "confirmedAt")
    private Timestamp confirmedAt;

    @Column(name = "confirmation_sent_at")
    @ApiModelProperty(value = "confirmationSentAt")
    private Timestamp confirmationSentAt;

    @Column(name = "unconfirmed_email")
    @ApiModelProperty(value = "unconfirmedEmail")
    private String unconfirmedEmail;

    @Column(name = "failed_attempts",nullable = false)
    @NotNull
    @ApiModelProperty(value = "failedAttempts")
    private Integer failedAttempts;

    @Column(name = "unlock_token")
    @ApiModelProperty(value = "unlockToken")
    private String unlockToken;

    @Column(name = "locked_at")
    @ApiModelProperty(value = "lockedAt")
    private Timestamp lockedAt;

    @Column(name = "sign_in_count")
    @ApiModelProperty(value = "signInCount")
    private Integer signInCount;

    @Column(name = "current_sign_in_at")
    @ApiModelProperty(value = "currentSignInAt")
    private Timestamp currentSignInAt;

    @Column(name = "last_sign_in_at")
    @ApiModelProperty(value = "lastSignInAt")
    private Timestamp lastSignInAt;

    @Column(name = "current_sign_in_ip")
    @ApiModelProperty(value = "currentSignInIp")
    private String currentSignInIp;

    @Column(name = "last_sign_in_ip")
    @ApiModelProperty(value = "lastSignInIp")
    private String lastSignInIp;

    @Column(name = "name")
    @ApiModelProperty(value = "name")
    private String name;

    @Column(name = "nickname")
    @ApiModelProperty(value = "nickname")
    private String nickname;

    @Column(name = "avatar")
    @ApiModelProperty(value = "avatar")
    private String avatar;

    @Column(name = "email")
    @ApiModelProperty(value = "email")
    private String email;

    @Column(name = "mobile")
    @ApiModelProperty(value = "mobile")
    private String mobile;

    @Column(name = "mina_openid")
    @ApiModelProperty(value = "minaOpenid")
    private String minaOpenid;

    @Column(name = "wx_unionid")
    @ApiModelProperty(value = "wxUnionid")
    private String wxUnionid;

    @Column(name = "tokens")
    @ApiModelProperty(value = "tokens")
    private String tokens;

    @Column(name = "created_at",nullable = false)
    @NotNull
    @ApiModelProperty(value = "createdAt")
    private Timestamp createdAt;

    @Column(name = "updated_at",nullable = false)
    @NotNull
    @ApiModelProperty(value = "updatedAt")
    private Timestamp updatedAt;

    @Column(name = "company_id")
    @ApiModelProperty(value = "companyId")
    private Long companyId;
}