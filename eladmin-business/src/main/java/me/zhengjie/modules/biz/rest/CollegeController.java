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
import me.zhengjie.api.domain.biz.College;
import me.zhengjie.modules.biz.service.CollegeService;
import me.zhengjie.modules.biz.service.dto.CollegeQueryCriteria;
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
@Api(tags = "College管理")
@RequestMapping("/api/college")
public class CollegeController {

    private final CollegeService collegeService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('college:list')")
    public void download(HttpServletResponse response, CollegeQueryCriteria criteria) throws IOException {
        collegeService.download(collegeService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询College")
    @ApiOperation("查询College")
    @PreAuthorize("@el.check('college:list')")
    public ResponseEntity<Object> query(CollegeQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(collegeService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增College")
    @ApiOperation("新增College")
    @PreAuthorize("@el.check('college:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody College resources){
        return new ResponseEntity<>(collegeService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改College")
    @ApiOperation("修改College")
    @PreAuthorize("@el.check('college:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody College resources){
        collegeService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除College")
    @ApiOperation("删除College")
    @PreAuthorize("@el.check('college:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        collegeService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}