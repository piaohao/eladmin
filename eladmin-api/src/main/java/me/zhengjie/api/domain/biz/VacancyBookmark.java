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
@Table(name = "biz_vacancy_bookmark")
public class VacancyBookmark implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ApiModelProperty(value = "id")
    private Long id;

//    @Column(name = "vacancy_id")
    @ApiModelProperty(value = "vacancyId")
    @ManyToOne
    @JoinColumn(name = "vacancy_id")
    private Vacancy vacancy;
//    private Long vacancyId;

//    @Column(name = "student_user_id")
    @ApiModelProperty(value = "studentUserId")
    @ManyToOne
    @JoinColumn(name = "student_user_id")
    private StudentUser studentUser;
//    private Long studentUserId;

    @Column(name = "created_at", nullable = false)
    @NotNull
    @ApiModelProperty(value = "createdAt")
    private Timestamp createdAt;

    @Column(name = "updated_at", nullable = false)
    @NotNull
    @ApiModelProperty(value = "updatedAt")
    private Timestamp updatedAt;
}