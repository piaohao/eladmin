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
import me.zhengjie.api.domain.biz.ResumeSummarize;
import me.zhengjie.modules.biz.service.ResumeSummarizeService;
import me.zhengjie.modules.biz.service.dto.ResumeSummarizeQueryCriteria;
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
@Api(tags = "ResumeSummarize管理")
@RequestMapping("/api/resumeSummarize")
public class ResumeSummarizeController {

    private final ResumeSummarizeService resumeSummarizeService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('resumeSummarize:list')")
    public void download(HttpServletResponse response, ResumeSummarizeQueryCriteria criteria) throws IOException {
        resumeSummarizeService.download(resumeSummarizeService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询ResumeSummarize")
    @ApiOperation("查询ResumeSummarize")
    @PreAuthorize("@el.check('resumeSummarize:list')")
    public ResponseEntity<Object> query(ResumeSummarizeQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(resumeSummarizeService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增ResumeSummarize")
    @ApiOperation("新增ResumeSummarize")
    @PreAuthorize("@el.check('resumeSummarize:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody ResumeSummarize resources){
        return new ResponseEntity<>(resumeSummarizeService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改ResumeSummarize")
    @ApiOperation("修改ResumeSummarize")
    @PreAuthorize("@el.check('resumeSummarize:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody ResumeSummarize resources){
        resumeSummarizeService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除ResumeSummarize")
    @ApiOperation("删除ResumeSummarize")
    @PreAuthorize("@el.check('resumeSummarize:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        resumeSummarizeService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}