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
import me.zhengjie.api.domain.biz.ResumeIntention;
import me.zhengjie.modules.biz.service.ResumeIntentionService;
import me.zhengjie.modules.biz.service.dto.ResumeIntentionQueryCriteria;
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
@Api(tags = "resume_intention管理")
@RequestMapping("/api/ResumeIntention")
public class ResumeIntentionController {

    private final ResumeIntentionService ResumeIntentionService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('ResumeIntention:list')")
    public void download(HttpServletResponse response, ResumeIntentionQueryCriteria criteria) throws IOException {
        ResumeIntentionService.download(ResumeIntentionService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询resume_intention")
    @ApiOperation("查询resume_intention")
    @PreAuthorize("@el.check('ResumeIntention:list')")
    public ResponseEntity<Object> query(ResumeIntentionQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(ResumeIntentionService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增resume_intention")
    @ApiOperation("新增resume_intention")
    @PreAuthorize("@el.check('ResumeIntention:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody ResumeIntention resources){
        return new ResponseEntity<>(ResumeIntentionService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改resume_intention")
    @ApiOperation("修改resume_intention")
    @PreAuthorize("@el.check('ResumeIntention:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody ResumeIntention resources){
        ResumeIntentionService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除resume_intention")
    @ApiOperation("删除resume_intention")
    @PreAuthorize("@el.check('ResumeIntention:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        ResumeIntentionService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}