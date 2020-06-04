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
* @date 2020-06-04
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "ApprenticeProjectApplicant管理")
@RequestMapping("/api/apprenticeProjectApplicant")
public class ApprenticeProjectApplicantController {

    private final ApprenticeProjectApplicantService apprenticeProjectApplicantService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('apprenticeProjectApplicant:list')")
    public void download(HttpServletResponse response, ApprenticeProjectApplicantQueryCriteria criteria) throws IOException {
        apprenticeProjectApplicantService.download(apprenticeProjectApplicantService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询ApprenticeProjectApplicant")
    @ApiOperation("查询ApprenticeProjectApplicant")
    @PreAuthorize("@el.check('apprenticeProjectApplicant:list')")
    public ResponseEntity<Object> query(ApprenticeProjectApplicantQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(apprenticeProjectApplicantService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增ApprenticeProjectApplicant")
    @ApiOperation("新增ApprenticeProjectApplicant")
    @PreAuthorize("@el.check('apprenticeProjectApplicant:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody ApprenticeProjectApplicant resources){
        return new ResponseEntity<>(apprenticeProjectApplicantService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改ApprenticeProjectApplicant")
    @ApiOperation("修改ApprenticeProjectApplicant")
    @PreAuthorize("@el.check('apprenticeProjectApplicant:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody ApprenticeProjectApplicant resources){
        apprenticeProjectApplicantService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除ApprenticeProjectApplicant")
    @ApiOperation("删除ApprenticeProjectApplicant")
    @PreAuthorize("@el.check('apprenticeProjectApplicant:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        apprenticeProjectApplicantService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}