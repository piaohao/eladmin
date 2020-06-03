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
import me.zhengjie.api.domain.biz.ResumeStatus;
import me.zhengjie.modules.biz.service.ResumeStatusService;
import me.zhengjie.modules.biz.service.dto.ResumeStatusQueryCriteria;
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
@Api(tags = "resume_status管理")
@RequestMapping("/api/ResumeStatus")
public class ResumeStatusController {

    private final ResumeStatusService ResumeStatusService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('ResumeStatus:list')")
    public void download(HttpServletResponse response, ResumeStatusQueryCriteria criteria) throws IOException {
        ResumeStatusService.download(ResumeStatusService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询resume_status")
    @ApiOperation("查询resume_status")
    @PreAuthorize("@el.check('ResumeStatus:list')")
    public ResponseEntity<Object> query(ResumeStatusQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(ResumeStatusService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增resume_status")
    @ApiOperation("新增resume_status")
    @PreAuthorize("@el.check('ResumeStatus:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody ResumeStatus resources){
        return new ResponseEntity<>(ResumeStatusService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改resume_status")
    @ApiOperation("修改resume_status")
    @PreAuthorize("@el.check('ResumeStatus:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody ResumeStatus resources){
        ResumeStatusService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除resume_status")
    @ApiOperation("删除resume_status")
    @PreAuthorize("@el.check('ResumeStatus:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        ResumeStatusService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}