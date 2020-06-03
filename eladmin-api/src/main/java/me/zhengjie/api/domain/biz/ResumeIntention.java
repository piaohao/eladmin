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
import java.io.Serializable;

/**
* @website https://el-admin.vip
* @description /
* @author piaohao
* @date 2020-06-03
**/
@Entity
@Data
@Table(name="biz_resume_intention")
public class ResumeIntention implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    @Column(name = "vcc_type_id")
    @ApiModelProperty(value = "职位类型id")
    private Long vccTypeId;

    @Column(name = "industry_id")
    @ApiModelProperty(value = "行业id")
    private Long industryId;

    @Column(name = "city_id")
    @ApiModelProperty(value = "城市id")
    private Long cityId;

    @Column(name = "vcc_property")
    @ApiModelProperty(value = "求职性质,campus,intern,all")
    private String vccProperty;

    @Column(name = "student_user_id")
    @ApiModelProperty(value = "studentUserId")
    private Long studentUserId;
}