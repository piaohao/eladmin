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
import me.zhengjie.api.domain.biz.ResumeProject;
import me.zhengjie.modules.biz.service.ResumeProjectService;
import me.zhengjie.modules.biz.service.dto.ResumeProjectQueryCriteria;
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
@Api(tags = "resume_project管理")
@RequestMapping("/api/ResumeProject")
public class ResumeProjectController {

    private final ResumeProjectService ResumeProjectService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('ResumeProject:list')")
    public void download(HttpServletResponse response, ResumeProjectQueryCriteria criteria) throws IOException {
        ResumeProjectService.download(ResumeProjectService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询resume_project")
    @ApiOperation("查询resume_project")
    @PreAuthorize("@el.check('ResumeProject:list')")
    public ResponseEntity<Object> query(ResumeProjectQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(ResumeProjectService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增resume_project")
    @ApiOperation("新增resume_project")
    @PreAuthorize("@el.check('ResumeProject:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody ResumeProject resources){
        return new ResponseEntity<>(ResumeProjectService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改resume_project")
    @ApiOperation("修改resume_project")
    @PreAuthorize("@el.check('ResumeProject:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody ResumeProject resources){
        ResumeProjectService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除resume_project")
    @ApiOperation("删除resume_project")
    @PreAuthorize("@el.check('ResumeProject:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        ResumeProjectService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}