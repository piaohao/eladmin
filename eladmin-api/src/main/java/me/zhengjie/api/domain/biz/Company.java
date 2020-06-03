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
@Table(name="biz_company")
public class Company implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    @Column(name = "name")
    @ApiModelProperty(value = "name")
    private String name;

    @Column(name = "logo")
    @ApiModelProperty(value = "logo")
    private String logo;

    @Column(name = "address")
    @ApiModelProperty(value = "address")
    private String address;

    @Column(name = "home_page")
    @ApiModelProperty(value = "homePage")
    private String homePage;

    @Column(name = "profile")
    @ApiModelProperty(value = "profile")
    private String profile;

    @Column(name = "tags")
    @ApiModelProperty(value = "tags")
    private String tags;

    @Column(name = "photos")
    @ApiModelProperty(value = "photos")
    private String photos;

    @Column(name = "lat")
    @ApiModelProperty(value = "lat")
    private Double lat;

    @Column(name = "lng")
    @ApiModelProperty(value = "lng")
    private Double lng;

    @Column(name = "created_at",nullable = false)
    @NotNull
    @ApiModelProperty(value = "createdAt")
    private Timestamp createdAt;

    @Column(name = "updated_at",nullable = false)
    @NotNull
    @ApiModelProperty(value = "updatedAt")
    private Timestamp updatedAt;

    @Column(name = "is_star")
    @ApiModelProperty(value = "isStar")
    private Integer isStar;

    @Column(name = "company_scale_id")
    @ApiModelProperty(value = "companyScaleId")
    private Long companyScaleId;

    @Column(name = "company_financing_id")
    @ApiModelProperty(value = "companyFinancingId")
    private Long companyFinancingId;

    @Column(name = "state_id")
    @ApiModelProperty(value = "stateId")
    private Long stateId;

    @Column(name = "city_id")
    @ApiModelProperty(value = "cityId")
    private Long cityId;

    @Column(name = "district_id")
    @ApiModelProperty(value = "districtId")
    private Long districtId;

    @Column(name = "industry_id")
    @ApiModelProperty(value = "industryId")
    private Long industryId;

    @Column(name = "welfare_id")
    @ApiModelProperty(value = "welfareId")
    private Long welfareId;
}