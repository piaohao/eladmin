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
import java.io.Serializable;

/**
* @website https://el-admin.vip
* @description /
* @author piaohao
* @date 2020-06-03
**/
@Entity
@Data
@Table(name="biz_resume_education")
public class ResumeEducation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    @Column(name = "resume_id")
    @ApiModelProperty(value = "resumeId")
    private Long resumeId;

    @Column(name = "school")
    @ApiModelProperty(value = "school")
    private String school;

    @Column(name = "major")
    @ApiModelProperty(value = "major")
    private String major;

    @Column(name = "education")
    @ApiModelProperty(value = "education")
    private String education;

    @Column(name = "enrollment_time")
    @ApiModelProperty(value = "enrollmentTime")
    private String enrollmentTime;

    @Column(name = "graduation_time")
    @ApiModelProperty(value = "graduationTime")
    private String graduationTime;

    @Column(name = "gpa")
    @ApiModelProperty(value = "gpa")
    private Double gpa;

    @Column(name = "score_ranking")
    @ApiModelProperty(value = "scoreRanking")
    private String scoreRanking;

    @Column(name = "major_course")
    @ApiModelProperty(value = "majorCourse")
    private String majorCourse;
}