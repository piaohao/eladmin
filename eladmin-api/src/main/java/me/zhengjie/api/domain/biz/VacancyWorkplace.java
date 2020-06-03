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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
* @website https://el-admin.vip
* @description /
* @author piaohao
* @date 2020-06-03
**/
@Entity
@Data
@Table(name="biz_vacancy_workplace")
public class VacancyWorkplace implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    @Column(name = "vacancy_id")
    @ApiModelProperty(value = "职位id")
    private Long vacancyId;

    @Column(name = "city_id",nullable = false)
    @NotNull
    @ApiModelProperty(value = "城市id")
    private Long cityId;

    @Column(name = "address",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "地址")
    private String address;

    @Column(name = "street")
    @ApiModelProperty(value = "街道")
    private String street;

    @Column(name = "longitude",nullable = false)
    @NotNull
    @ApiModelProperty(value = "经度")
    private Double longitude;

    @Column(name = "latitude",nullable = false)
    @NotNull
    @ApiModelProperty(value = "纬度")
    private Double latitude;
}