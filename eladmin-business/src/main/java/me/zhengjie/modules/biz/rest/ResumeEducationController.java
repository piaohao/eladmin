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
import me.zhengjie.api.domain.biz.ResumeEducation;
import me.zhengjie.modules.biz.service.ResumeEducationService;
import me.zhengjie.modules.biz.service.dto.ResumeEducationQueryCriteria;
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
@Api(tags = "ResumeEducation管理")
@RequestMapping("/api/resumeEducation")
public class ResumeEducationController {

    private final ResumeEducationService resumeEducationService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('resumeEducation:list')")
    public void download(HttpServletResponse response, ResumeEducationQueryCriteria criteria) throws IOException {
        resumeEducationService.download(resumeEducationService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询ResumeEducation")
    @ApiOperation("查询ResumeEducation")
    @PreAuthorize("@el.check('resumeEducation:list')")
    public ResponseEntity<Object> query(ResumeEducationQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(resumeEducationService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增ResumeEducation")
    @ApiOperation("新增ResumeEducation")
    @PreAuthorize("@el.check('resumeEducation:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody ResumeEducation resources){
        return new ResponseEntity<>(resumeEducationService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改ResumeEducation")
    @ApiOperation("修改ResumeEducation")
    @PreAuthorize("@el.check('resumeEducation:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody ResumeEducation resources){
        resumeEducationService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除ResumeEducation")
    @ApiOperation("删除ResumeEducation")
    @PreAuthorize("@el.check('resumeEducation:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        resumeEducationService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}