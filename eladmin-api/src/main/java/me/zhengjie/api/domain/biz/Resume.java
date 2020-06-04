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
import me.zhengjie.api.domain.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.io.Serializable;
import java.util.List;

/**
 * @author piaohao
 * @website https://el-admin.vip
 * @description /
 * @date 2020-06-04
 **/
@Entity
@Data
@Table(name = "biz_resume")
public class Resume implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ApiModelProperty(value = "id")
    private Long id;

//    @Column(name = "student_user_id")
    @ApiModelProperty(value = "studentUserId")
    @OneToOne
    @JoinColumn(name = "student_user_id")
    private StudentUser studentUser;
//    private Long studentUserId;

    @Column(name = "name")
    @ApiModelProperty(value = "name")
    private String name;

    @Column(name = "gender")
    @ApiModelProperty(value = "gender")
    private String gender;

    @Column(name = "date_of_birth")
    @ApiModelProperty(value = "dateOfBirth")
    private String dateOfBirth;

    @Column(name = "mobile")
    @ApiModelProperty(value = "mobile")
    private String mobile;

    @Column(name = "created_at", nullable = false)
    @NotNull
    @ApiModelProperty(value = "createdAt")
    private Timestamp createdAt;

    @Column(name = "updated_at", nullable = false)
    @NotNull
    @ApiModelProperty(value = "updatedAt")
    private Timestamp updatedAt;

//    @Column(name = "city_id")
    @ApiModelProperty(value = "城市id")
    @OneToOne
    @JoinColumn(name = "city_id")
    private City city;
//    private Long cityId;

    @Column(name = "email")
    @ApiModelProperty(value = "邮箱")
    private String email;

    @Column(name = "avatar")
    @ApiModelProperty(value = "头像")
    private String avatar;

    @OneToMany(mappedBy = "resume")
    private List<ResumeAcademic> academics;

    @OneToMany(mappedBy = "resume")
    private List<ResumeEducation> educations;

    @OneToMany(mappedBy = "resume")
    private List<ResumeIntern> interns;

    @OneToMany(mappedBy = "resume")
    private List<ResumePrize> prizes;

    @OneToMany(mappedBy = "resume")
    private List<ResumeProject> projects;

    @OneToMany(mappedBy = "resume")
    private List<ResumeSkill> skills;

    @OneToMany(mappedBy = "resume")
    private List<ResumeWork> works;

    @OneToOne(mappedBy = "resume")
    private ResumeInterest interest;

    @OneToOne(mappedBy = "resume")
    private ResumeSummarize summarize;
}