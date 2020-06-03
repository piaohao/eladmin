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

import java.io.Serializable;
import java.sql.Timestamp;

/**
* @website https://el-admin.vip
* @description /
* @author piaohao
* @date 2020-06-03
**/
@Data
public class ApprenticeProjectDto implements Serializable {

    private Long id;

    private String schoolIds;

    private Long companyId;

    private String name;

    private Timestamp startAt;

    private Timestamp endAt;

    private String brief;

    private String departments;

    private Integer headCount;

    /** 附件url */
    private String attachmentUrl;

    private String mentorIds;

    private String status;

    private Timestamp createdAt;

    private Timestamp updatedAt;
}