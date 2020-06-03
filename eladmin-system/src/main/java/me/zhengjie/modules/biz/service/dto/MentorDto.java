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
* @date 2020-06-03
**/
@Data
public class MentorDto implements Serializable {

    private Long id;

    private Long companyId;

    private String name;

    private String gender;

    private Integer age;

    private String photo;

    private String joinedAt;

    private String tags;

    private String contactMobile;

    private String position;

    private String profile;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    /** 专业id */
    private Long majorId;
}