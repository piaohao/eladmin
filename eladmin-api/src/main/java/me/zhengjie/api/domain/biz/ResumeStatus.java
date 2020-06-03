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
@Table(name="biz_resume_status")
public class ResumeStatus implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    @Column(name = "arrival_time")
    @ApiModelProperty(value = "到岗时间")
    private String arrivalTime;

    @Column(name = "intern_duration")
    @ApiModelProperty(value = "实习时长")
    private String internDuration;

    @Column(name = "weekly_attendance")
    @ApiModelProperty(value = "每周出勤")
    private String weeklyAttendance;

    @Column(name = "student_user_id")
    @ApiModelProperty(value = "studentUserId")
    private Long studentUserId;
}