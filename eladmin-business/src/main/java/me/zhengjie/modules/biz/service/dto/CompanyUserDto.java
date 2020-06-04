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
package me.zhengjie.modules.biz.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @website https://el-admin.vip
* @description /
* @author piaohao
* @date 2020-06-04
**/
@Data
public class CompanyUserDto implements Serializable {

    private Long id;

    private String provider;

    private String uid;

    private String encryptedPassword;

    private String resetPasswordToken;

    private Timestamp resetPasswordSentAt;

    private Integer allowPasswordChange;

    private Timestamp rememberCreatedAt;

    private String confirmationToken;

    private Timestamp confirmedAt;

    private Timestamp confirmationSentAt;

    private String unconfirmedEmail;

    private Integer failedAttempts;

    private String unlockToken;

    private Timestamp lockedAt;

    private Integer signInCount;

    private Timestamp currentSignInAt;

    private Timestamp lastSignInAt;

    private String currentSignInIp;

    private String lastSignInIp;

    private String name;

    private String nickname;

    private String avatar;

    private String email;

    private String mobile;

    private String minaOpenid;

    private String wxUnionid;

    private String tokens;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    private Long companyId;
}