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

import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @website https://el-admin.vip
* @description /
* @author piaohao
* @date 2020-06-03
**/
@Entity
@Data
@Table(name="biz_apprentice_project")
public class ApprenticeProject implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    @Column(name = "school_ids")
    @ApiModelProperty(value = "schoolIds")
    private String schoolIds;

    @Column(name = "company_id")
    @ApiModelProperty(value = "companyId")
    private Long companyId;

    @Column(name = "name")
    @ApiModelProperty(value = "name")
    private String name;

    @Column(name = "start_at")
    @ApiModelProperty(value = "startAt")
    private Timestamp startAt;

    @Column(name = "end_at")
    @ApiModelProperty(value = "endAt")
    private Timestamp endAt;

    @Column(name = "brief")
    @ApiModelProperty(value = "brief")
    private String brief;

    @Column(name = "departments")
    @ApiModelProperty(value = "departments")
    private String departments;

    @Column(name = "head_count")
    @ApiModelProperty(value = "headCount")
    private Integer headCount;

    @Column(name = "attachment_url")
    @ApiModelProperty(value = "附件url")
    private String attachmentUrl;

    @Column(name = "mentor_ids")
    @ApiModelProperty(value = "mentorIds")
    private String mentorIds;

    @Column(name = "status")
    @ApiModelProperty(value = "status")
    private String status;

    @Column(name = "created_at",nullable = false)
    @NotNull
    @ApiModelProperty(value = "createdAt")
    private Timestamp createdAt;

    @Column(name = "updated_at",nullable = false)
    @NotNull
    @ApiModelProperty(value = "updatedAt")
    private Timestamp updatedAt;
}