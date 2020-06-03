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
@Table(name="biz_vcc_applicant")
public class VccApplicant implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    @Column(name = "resume_id")
    @ApiModelProperty(value = "resumeId")
    private Long resumeId;

    @Column(name = "vacancy_id")
    @ApiModelProperty(value = "vacancyId")
    private Long vacancyId;

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

    @Column(name = "will_interview_at")
    @ApiModelProperty(value = "willInterviewAt")
    private Timestamp willInterviewAt;

    @Column(name = "interview_comment")
    @ApiModelProperty(value = "interviewComment")
    private String interviewComment;

    @Column(name = "timeline")
    @ApiModelProperty(value = "timeline")
    private String timeline;

    @Column(name = "interview_place")
    @ApiModelProperty(value = "面试地点")
    private String interviewPlace;

    @Column(name = "interview_person")
    @ApiModelProperty(value = "联系人")
    private String interviewPerson;

    @Column(name = "interview_mobile")
    @ApiModelProperty(value = "面试电话")
    private String interviewMobile;

    @Column(name = "hire_comment")
    @ApiModelProperty(value = "录用说明")
    private String hireComment;

    @Column(name = "improper_comment")
    @ApiModelProperty(value = "不合适说明")
    private String improperComment;
}