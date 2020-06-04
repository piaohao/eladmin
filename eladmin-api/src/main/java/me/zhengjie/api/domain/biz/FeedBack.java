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
@Table(name="biz_feed_back")
public class FeedBack implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ApiModelProperty(value = "id")
    private Long id;

//    @Column(name = "student_user_id")
    @ApiModelProperty(value = "studentUserId")
    @ManyToOne
    @JoinColumn(name="student_user_id")
    private StudentUser studentUser;
//    private Long studentUserId;

    @Column(name = "type")
    @ApiModelProperty(value = "type")
    private String type;

    @Column(name = "content")
    @ApiModelProperty(value = "content")
    private String content;

    @Column(name = "mobile")
    @ApiModelProperty(value = "mobile")
    private String mobile;

    @Column(name = "images")
    @ApiModelProperty(value = "images")
    private String images;

    @Column(name = "allow_contact")
    @ApiModelProperty(value = "allowContact")
    private Integer allowContact;

    @Column(name = "created_at",nullable = false)
    @NotNull
    @ApiModelProperty(value = "createdAt")
    private Timestamp createdAt;

    @Column(name = "updated_at",nullable = false)
    @NotNull
    @ApiModelProperty(value = "updatedAt")
    private Timestamp updatedAt;
}