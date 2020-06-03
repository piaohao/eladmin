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
package me.zhengjie.modules.biz.rest;

import me.zhengjie.annotation.Log;
import me.zhengjie.api.domain.biz.ApprenticeProjectApplicant;
import me.zhengjie.modules.biz.service.ApprenticeProjectApplicantService;
import me.zhengjie.modules.biz.service.dto.ApprenticeProjectApplicantQueryCriteria;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @website https://el-admin.vip
* @author piaohao
* @date 2020-06-03
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "apprentice_project_applicant管理")
@RequestMapping("/api/ApprenticeProjectApplicant")
public class ApprenticeProjectApplicantController {

    private final ApprenticeProjectApplicantService ApprenticeProjectApplicantService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('ApprenticeProjectApplicant:list')")
    public void download(HttpServletResponse response, ApprenticeProjectApplicantQueryCriteria criteria) throws IOException {
        ApprenticeProjectApplicantService.download(ApprenticeProjectApplicantService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询apprentice_project_applicant")
    @ApiOperation("查询apprentice_project_applicant")
    @PreAuthorize("@el.check('ApprenticeProjectApplicant:list')")
    public ResponseEntity<Object> query(ApprenticeProjectApplicantQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(ApprenticeProjectApplicantService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增apprentice_project_applicant")
    @ApiOperation("新增apprentice_project_applicant")
    @PreAuthorize("@el.check('ApprenticeProjectApplicant:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody ApprenticeProjectApplicant resources){
        return new ResponseEntity<>(ApprenticeProjectApplicantService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改apprentice_project_applicant")
    @ApiOperation("修改apprentice_project_applicant")
    @PreAuthorize("@el.check('ApprenticeProjectApplicant:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody ApprenticeProjectApplicant resources){
        ApprenticeProjectApplicantService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除apprentice_project_applicant")
    @ApiOperation("删除apprentice_project_applicant")
    @PreAuthorize("@el.check('ApprenticeProjectApplicant:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        ApprenticeProjectApplicantService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}