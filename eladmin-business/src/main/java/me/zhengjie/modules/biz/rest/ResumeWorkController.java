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
import me.zhengjie.api.domain.biz.ResumeWork;
import me.zhengjie.modules.biz.service.ResumeWorkService;
import me.zhengjie.modules.biz.service.dto.ResumeWorkQueryCriteria;
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
@Api(tags = "ResumeWork管理")
@RequestMapping("/api/resumeWork")
public class ResumeWorkController {

    private final ResumeWorkService resumeWorkService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('resumeWork:list')")
    public void download(HttpServletResponse response, ResumeWorkQueryCriteria criteria) throws IOException {
        resumeWorkService.download(resumeWorkService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询ResumeWork")
    @ApiOperation("查询ResumeWork")
    @PreAuthorize("@el.check('resumeWork:list')")
    public ResponseEntity<Object> query(ResumeWorkQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(resumeWorkService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增ResumeWork")
    @ApiOperation("新增ResumeWork")
    @PreAuthorize("@el.check('resumeWork:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody ResumeWork resources){
        return new ResponseEntity<>(resumeWorkService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改ResumeWork")
    @ApiOperation("修改ResumeWork")
    @PreAuthorize("@el.check('resumeWork:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody ResumeWork resources){
        resumeWorkService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除ResumeWork")
    @ApiOperation("删除ResumeWork")
    @PreAuthorize("@el.check('resumeWork:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        resumeWorkService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}