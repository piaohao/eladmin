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
* @date 2020-06-04
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "School管理")
@RequestMapping("/api/school")
public class SchoolController {

    private final SchoolService schoolService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('school:list')")
    public void download(HttpServletResponse response, SchoolQueryCriteria criteria) throws IOException {
        schoolService.download(schoolService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询School")
    @ApiOperation("查询School")
    @PreAuthorize("@el.check('school:list')")
    public ResponseEntity<Object> query(SchoolQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(schoolService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增School")
    @ApiOperation("新增School")
    @PreAuthorize("@el.check('school:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody School resources){
        return new ResponseEntity<>(schoolService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改School")
    @ApiOperation("修改School")
    @PreAuthorize("@el.check('school:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody School resources){
        schoolService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除School")
    @ApiOperation("删除School")
    @PreAuthorize("@el.check('school:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        schoolService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}