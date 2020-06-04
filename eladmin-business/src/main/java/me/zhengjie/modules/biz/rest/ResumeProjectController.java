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
import me.zhengjie.api.domain.biz.ResumeProject;
import me.zhengjie.modules.biz.service.ResumeProjectService;
import me.zhengjie.modules.biz.service.dto.ResumeProjectQueryCriteria;
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
@Api(tags = "ResumeProject管理")
@RequestMapping("/api/resumeProject")
public class ResumeProjectController {

    private final ResumeProjectService resumeProjectService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('resumeProject:list')")
    public void download(HttpServletResponse response, ResumeProjectQueryCriteria criteria) throws IOException {
        resumeProjectService.download(resumeProjectService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询ResumeProject")
    @ApiOperation("查询ResumeProject")
    @PreAuthorize("@el.check('resumeProject:list')")
    public ResponseEntity<Object> query(ResumeProjectQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(resumeProjectService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增ResumeProject")
    @ApiOperation("新增ResumeProject")
    @PreAuthorize("@el.check('resumeProject:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody ResumeProject resources){
        return new ResponseEntity<>(resumeProjectService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改ResumeProject")
    @ApiOperation("修改ResumeProject")
    @PreAuthorize("@el.check('resumeProject:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody ResumeProject resources){
        resumeProjectService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除ResumeProject")
    @ApiOperation("删除ResumeProject")
    @PreAuthorize("@el.check('resumeProject:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        resumeProjectService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}