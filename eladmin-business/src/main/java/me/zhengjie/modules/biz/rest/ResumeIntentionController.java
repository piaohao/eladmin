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
import me.zhengjie.api.domain.biz.ResumeIntention;
import me.zhengjie.modules.biz.service.ResumeIntentionService;
import me.zhengjie.modules.biz.service.dto.ResumeIntentionQueryCriteria;
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
@Api(tags = "ResumeIntention管理")
@RequestMapping("/api/resumeIntention")
public class ResumeIntentionController {

    private final ResumeIntentionService resumeIntentionService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('resumeIntention:list')")
    public void download(HttpServletResponse response, ResumeIntentionQueryCriteria criteria) throws IOException {
        resumeIntentionService.download(resumeIntentionService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询ResumeIntention")
    @ApiOperation("查询ResumeIntention")
    @PreAuthorize("@el.check('resumeIntention:list')")
    public ResponseEntity<Object> query(ResumeIntentionQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(resumeIntentionService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增ResumeIntention")
    @ApiOperation("新增ResumeIntention")
    @PreAuthorize("@el.check('resumeIntention:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody ResumeIntention resources){
        return new ResponseEntity<>(resumeIntentionService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改ResumeIntention")
    @ApiOperation("修改ResumeIntention")
    @PreAuthorize("@el.check('resumeIntention:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody ResumeIntention resources){
        resumeIntentionService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除ResumeIntention")
    @ApiOperation("删除ResumeIntention")
    @PreAuthorize("@el.check('resumeIntention:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        resumeIntentionService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}