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
import me.zhengjie.api.domain.biz.School;
import me.zhengjie.modules.biz.service.SchoolService;
import me.zhengjie.modules.biz.service.dto.SchoolQueryCriteria;
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
@Api(tags = "school管理")
@RequestMapping("/api/School")
public class SchoolController {

    private final SchoolService SchoolService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('School:list')")
    public void download(HttpServletResponse response, SchoolQueryCriteria criteria) throws IOException {
        SchoolService.download(SchoolService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询school")
    @ApiOperation("查询school")
    @PreAuthorize("@el.check('School:list')")
    public ResponseEntity<Object> query(SchoolQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(SchoolService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增school")
    @ApiOperation("新增school")
    @PreAuthorize("@el.check('School:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody School resources){
        return new ResponseEntity<>(SchoolService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改school")
    @ApiOperation("修改school")
    @PreAuthorize("@el.check('School:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody School resources){
        SchoolService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除school")
    @ApiOperation("删除school")
    @PreAuthorize("@el.check('School:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        SchoolService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}