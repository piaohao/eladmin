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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.api.domain.biz.ResumeAcademic;
import me.zhengjie.modules.biz.service.ResumeAcademicService;
import me.zhengjie.modules.biz.service.dto.ResumeAcademicQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
* @website https://el-admin.vip
* @author piaohao
* @date 2020-06-03
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "resume_academic管理")
@RequestMapping("/api/ResumeAcademic")
public class ResumeAcademicController {

    private final ResumeAcademicService ResumeAcademicService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('ResumeAcademic:list')")
    public void download(HttpServletResponse response, ResumeAcademicQueryCriteria criteria) throws IOException {
        ResumeAcademicService.download(ResumeAcademicService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询resume_academic")
    @ApiOperation("查询resume_academic")
    @PreAuthorize("@el.check('ResumeAcademic:list')")
    public ResponseEntity<Object> query(ResumeAcademicQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(ResumeAcademicService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增resume_academic")
    @ApiOperation("新增resume_academic")
    @PreAuthorize("@el.check('ResumeAcademic:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody ResumeAcademic resources){
        return new ResponseEntity<>(ResumeAcademicService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改resume_academic")
    @ApiOperation("修改resume_academic")
    @PreAuthorize("@el.check('ResumeAcademic:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody ResumeAcademic resources){
        ResumeAcademicService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除resume_academic")
    @ApiOperation("删除resume_academic")
    @PreAuthorize("@el.check('ResumeAcademic:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        ResumeAcademicService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}