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
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @website https://el-admin.vip
* @description /
* @author piaohao
* @date 2020-06-04
**/
@Entity
@Data
@Table(name="biz_mentor")
public class Mentor implements Serializable {

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

    @Column(name = "gender")
    @ApiModelProperty(value = "gender")
    private String gender;

    @Column(name = "age")
    @ApiModelProperty(value = "age")
    private Integer age;

    @Column(name = "photo")
    @ApiModelProperty(value = "photo")
    private String photo;

    @Column(name = "joined_at")
    @ApiModelProperty(value = "joinedAt")
    private String joinedAt;

    @Column(name = "tags")
    @ApiModelProperty(value = "tags")
    private String tags;

    @Column(name = "contact_mobile")
    @ApiModelProperty(value = "contactMobile")
    private String contactMobile;

    @Column(name = "position")
    @ApiModelProperty(value = "position")
    private String position;

    @Column(name = "profile")
    @ApiModelProperty(value = "profile")
    private String profile;

    @Column(name = "created_at",nullable = false)
    @NotNull
    @ApiModelProperty(value = "createdAt")
    private Timestamp createdAt;

    @Column(name = "updated_at",nullable = false)
    @NotNull
    @ApiModelProperty(value = "updatedAt")
    private Timestamp updatedAt;

    @Column(name = "major_id")
    @ApiModelProperty(value = "专业id")
    private Long majorId;
}