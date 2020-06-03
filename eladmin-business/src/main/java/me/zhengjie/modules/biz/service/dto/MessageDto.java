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
package me.zhengjie.modules.biz.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
* @website https://el-admin.vip
* @description /
* @author piaohao
* @date 2020-06-03
**/
@Data
public class MessageDto implements Serializable {

    private Long id;

    /** 消息标题 */
    private String title;

    /** 消息类型,1系统公告，2职位消息，3交易消息 */
    private Integer type;

    /** 消息内容 */
    private String content;

    /** 发送方式,1所有企业，2部分企业 */
    private Integer delivery;

    private Timestamp createdAt;

    private Timestamp updatedAt;
}