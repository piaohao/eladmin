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
import me.zhengjie.api.domain.biz.ResumeIntern;
import me.zhengjie.modules.biz.service.ResumeInternService;
import me.zhengjie.modules.biz.service.dto.ResumeInternQueryCriteria;
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
@Api(tags = "resume_intern管理")
@RequestMapping("/api/ResumeIntern")
public class ResumeInternController {

    private final ResumeInternService ResumeInternService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('ResumeIntern:list')")
    public void download(HttpServletResponse response, ResumeInternQueryCriteria criteria) throws IOException {
        ResumeInternService.download(ResumeInternService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询resume_intern")
    @ApiOperation("查询resume_intern")
    @PreAuthorize("@el.check('ResumeIntern:list')")
    public ResponseEntity<Object> query(ResumeInternQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(ResumeInternService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增resume_intern")
    @ApiOperation("新增resume_intern")
    @PreAuthorize("@el.check('ResumeIntern:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody ResumeIntern resources){
        return new ResponseEntity<>(ResumeInternService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改resume_intern")
    @ApiOperation("修改resume_intern")
    @PreAuthorize("@el.check('ResumeIntern:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody ResumeIntern resources){
        ResumeInternService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除resume_intern")
    @ApiOperation("删除resume_intern")
    @PreAuthorize("@el.check('ResumeIntern:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        ResumeInternService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}