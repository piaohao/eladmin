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
@Table(name="biz_company_message")
public class CompanyMessage implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ApiModelProperty(value = "id")
    private Long id;

//    @Column(name = "company_id")
    @ApiModelProperty(value = "companyId")
    @ManyToOne
    @JoinColumn(name="company_id")
    private Company company;
//    private Long companyId;

//    @Column(name = "message_id")
    @ApiModelProperty(value = "messageId")
    @ManyToOne
    @JoinColumn(name="message_id")
    private Message message;
//    private Long messageId;

    @Column(name = "is_read")
    @ApiModelProperty(value = "isRead")
    private Boolean isRead;

    @Column(name = "created_at")
    @ApiModelProperty(value = "createdAt")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    @ApiModelProperty(value = "updatedAt")
    private Timestamp updatedAt;
}