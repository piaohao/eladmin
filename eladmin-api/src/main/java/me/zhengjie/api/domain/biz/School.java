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
import java.io.Serializable;

/**
 * @author piaohao
 * @website https://el-admin.vip
 * @description /
 * @date 2020-06-04
 **/
@Entity
@Data
@Table(name = "biz_school")
public class School implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    @Column(name = "name")
    @ApiModelProperty(value = "name")
    private String name;

//    @Column(name = "city_id")
    @ApiModelProperty(value = "cityId")
    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;
//    private Long cityId;

//    @Column(name = "state_id")
    @ApiModelProperty(value = "stateId")
    @ManyToOne
    @JoinColumn(name = "state_id")
    private State state;
//    private Long stateId;

//    @Column(name = "district_id")
    @ApiModelProperty(value = "districtId")
    @ManyToOne
    @JoinColumn(name = "district_id")
    private District district;
//    private Long districtId;
}