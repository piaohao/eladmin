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
import me.zhengjie.api.domain.biz.ResumeAcademic;
import me.zhengjie.modules.biz.service.ResumeAcademicService;
import me.zhengjie.modules.biz.service.dto.ResumeAcademicQueryCriteria;
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
@Api(tags = "ResumeAcademic管理")
@RequestMapping("/api/resumeAcademic")
public class ResumeAcademicController {

    private final ResumeAcademicService resumeAcademicService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('resumeAcademic:list')")
    public void download(HttpServletResponse response, ResumeAcademicQueryCriteria criteria) throws IOException {
        resumeAcademicService.download(resumeAcademicService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询ResumeAcademic")
    @ApiOperation("查询ResumeAcademic")
    @PreAuthorize("@el.check('resumeAcademic:list')")
    public ResponseEntity<Object> query(ResumeAcademicQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(resumeAcademicService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增ResumeAcademic")
    @ApiOperation("新增ResumeAcademic")
    @PreAuthorize("@el.check('resumeAcademic:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody ResumeAcademic resources){
        return new ResponseEntity<>(resumeAcademicService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改ResumeAcademic")
    @ApiOperation("修改ResumeAcademic")
    @PreAuthorize("@el.check('resumeAcademic:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody ResumeAcademic resources){
        resumeAcademicService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除ResumeAcademic")
    @ApiOperation("删除ResumeAcademic")
    @PreAuthorize("@el.check('resumeAcademic:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        resumeAcademicService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}