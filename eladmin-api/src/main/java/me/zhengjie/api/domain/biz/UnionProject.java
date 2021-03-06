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
 * @author piaohao
 * @website https://el-admin.vip
 * @description /
 * @date 2020-06-04
 **/
@Entity
@Data
@Table(name = "biz_union_project")
public class UnionProject implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ApiModelProperty(value = "id")
    private Long id;

//    @Column(name = "school_id")
    @ApiModelProperty(value = "schoolId")
    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school;
//    private Long schoolId;

//    @Column(name = "college_id")
    @ApiModelProperty(value = "学院id")
    @ManyToOne
    @JoinColumn(name = "college_id")
    private College college;
//    private Long collegeId;

//    @Column(name = "company_id")
    @ApiModelProperty(value = "companyId")
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
//    private Long companyId;

//    @Column(name = "vacancy_id")
    @ApiModelProperty(value = "职位id")
    @ManyToOne
    @JoinColumn(name = "vacancy_id")
    private Vacancy vacancy;
//    private Long vacancyId;

    @Column(name = "co_type")
    @ApiModelProperty(value = "coType")
    private String coType;

    @Column(name = "co_duration")
    @ApiModelProperty(value = "coDuration")
    private String coDuration;

    @Column(name = "status")
    @ApiModelProperty(value = "status")
    private String status;

    @Column(name = "created_at", nullable = false)
    @NotNull
    @ApiModelProperty(value = "createdAt")
    private Timestamp createdAt;

    @Column(name = "updated_at", nullable = false)
    @NotNull
    @ApiModelProperty(value = "updatedAt")
    private Timestamp updatedAt;
}