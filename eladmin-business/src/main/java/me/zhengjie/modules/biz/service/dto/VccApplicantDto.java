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
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @website https://el-admin.vip
* @description /
* @author piaohao
* @date 2020-06-04
**/
@Data
public class VccApplicantDto implements Serializable {

    private Long id;

    private Long resumeId;

    private Long vacancyId;

    private String status;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    private Timestamp willInterviewAt;

    private String interviewComment;

    private String timeline;

    /** 面试地点 */
    private String interviewPlace;

    /** 联系人 */
    private String interviewPerson;

    /** 面试电话 */
    private String interviewMobile;

    /** 录用说明 */
    private String hireComment;

    /** 不合适说明 */
    private String improperComment;
}