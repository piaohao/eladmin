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
* @date 2020-06-03
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "resume_work管理")
@RequestMapping("/api/ResumeWork")
public class ResumeWorkController {

    private final ResumeWorkService ResumeWorkService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('ResumeWork:list')")
    public void download(HttpServletResponse response, ResumeWorkQueryCriteria criteria) throws IOException {
        ResumeWorkService.download(ResumeWorkService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询resume_work")
    @ApiOperation("查询resume_work")
    @PreAuthorize("@el.check('ResumeWork:list')")
    public ResponseEntity<Object> query(ResumeWorkQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(ResumeWorkService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增resume_work")
    @ApiOperation("新增resume_work")
    @PreAuthorize("@el.check('ResumeWork:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody ResumeWork resources){
        return new ResponseEntity<>(ResumeWorkService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改resume_work")
    @ApiOperation("修改resume_work")
    @PreAuthorize("@el.check('ResumeWork:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody ResumeWork resources){
        ResumeWorkService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除resume_work")
    @ApiOperation("删除resume_work")
    @PreAuthorize("@el.check('ResumeWork:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        ResumeWorkService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}