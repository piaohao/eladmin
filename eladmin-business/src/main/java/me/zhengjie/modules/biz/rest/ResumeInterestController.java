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
import me.zhengjie.api.domain.biz.ResumeInterest;
import me.zhengjie.modules.biz.service.ResumeInterestService;
import me.zhengjie.modules.biz.service.dto.ResumeInterestQueryCriteria;
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
@Api(tags = "ResumeInterest管理")
@RequestMapping("/api/resumeInterest")
public class ResumeInterestController {

    private final ResumeInterestService resumeInterestService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('resumeInterest:list')")
    public void download(HttpServletResponse response, ResumeInterestQueryCriteria criteria) throws IOException {
        resumeInterestService.download(resumeInterestService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询ResumeInterest")
    @ApiOperation("查询ResumeInterest")
    @PreAuthorize("@el.check('resumeInterest:list')")
    public ResponseEntity<Object> query(ResumeInterestQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(resumeInterestService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增ResumeInterest")
    @ApiOperation("新增ResumeInterest")
    @PreAuthorize("@el.check('resumeInterest:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody ResumeInterest resources){
        return new ResponseEntity<>(resumeInterestService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改ResumeInterest")
    @ApiOperation("修改ResumeInterest")
    @PreAuthorize("@el.check('resumeInterest:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody ResumeInterest resources){
        resumeInterestService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除ResumeInterest")
    @ApiOperation("删除ResumeInterest")
    @PreAuthorize("@el.check('resumeInterest:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        resumeInterestService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}