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
@Table(name="biz_vacancy")
public class Vacancy implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    @Column(name = "company_id")
    @ApiModelProperty(value = "companyId")
    private Long companyId;

    @Column(name = "name")
    @ApiModelProperty(value = "name")
    private String name;

    @Column(name = "address")
    @ApiModelProperty(value = "address")
    private String address;

    @Column(name = "description")
    @ApiModelProperty(value = "description")
    private String description;

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

    @Column(name = "is_hottest")
    @ApiModelProperty(value = "isHottest")
    private Integer isHottest;

    @Column(name = "view_count")
    @ApiModelProperty(value = "viewCount")
    private Integer viewCount;

    @Column(name = "apply_count")
    @ApiModelProperty(value = "applyCount")
    private Integer applyCount;

    @Column(name = "state_id")
    @ApiModelProperty(value = "stateId")
    private Long stateId;

    @Column(name = "city_id")
    @ApiModelProperty(value = "cityId")
    private Long cityId;

    @Column(name = "district_id")
    @ApiModelProperty(value = "districtId")
    private Long districtId;

    @Column(name = "lat")
    @ApiModelProperty(value = "lat")
    private Double lat;

    @Column(name = "lng")
    @ApiModelProperty(value = "lng")
    private Double lng;

    @Column(name = "vcc_type_id")
    @ApiModelProperty(value = "vccTypeId")
    private Long vccTypeId;

    @Column(name = "education_id")
    @ApiModelProperty(value = "educationId")
    private Long educationId;

    @Column(name = "is_intern")
    @ApiModelProperty(value = "isIntern")
    private Integer isIntern;

    @Column(name = "intern_duration")
    @ApiModelProperty(value = "internDuration")
    private String internDuration;

    @Column(name = "salary_type")
    @ApiModelProperty(value = "日，月，年")
    private String salaryType;

    @Column(name = "salary_range_low")
    @ApiModelProperty(value = "salaryRangeLow")
    private Integer salaryRangeLow;

    @Column(name = "salary_range_high")
    @ApiModelProperty(value = "salaryRangeHigh")
    private Integer salaryRangeHigh;
}