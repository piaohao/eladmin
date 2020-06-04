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
import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;

/**
* @website https://el-admin.vip
* @description /
* @author piaohao
* @date 2020-06-04
**/
@Entity
@Data
@Table(name="biz_resume_project")
public class ResumeProject implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    @Column(name = "resume_id")
    @ApiModelProperty(value = "resumeId")
    private Long resumeId;

    @Column(name = "project")
    @ApiModelProperty(value = "project")
    private String project;

    @Column(name = "start_time")
    @ApiModelProperty(value = "startTime")
    private String startTime;

    @Column(name = "end_time")
    @ApiModelProperty(value = "endTime")
    private String endTime;

    @Column(name = "duty")
    @ApiModelProperty(value = "duty")
    private String duty;

    @Column(name = "description")
    @ApiModelProperty(value = "description")
    private String description;
}