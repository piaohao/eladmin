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
import me.zhengjie.api.domain.biz.SchoolUser;
import me.zhengjie.modules.biz.service.SchoolUserService;
import me.zhengjie.modules.biz.service.dto.SchoolUserQueryCriteria;
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
@Api(tags = "school_user管理")
@RequestMapping("/api/SchoolUser")
public class SchoolUserController {

    private final SchoolUserService SchoolUserService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('SchoolUser:list')")
    public void download(HttpServletResponse response, SchoolUserQueryCriteria criteria) throws IOException {
        SchoolUserService.download(SchoolUserService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询school_user")
    @ApiOperation("查询school_user")
    @PreAuthorize("@el.check('SchoolUser:list')")
    public ResponseEntity<Object> query(SchoolUserQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(SchoolUserService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增school_user")
    @ApiOperation("新增school_user")
    @PreAuthorize("@el.check('SchoolUser:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody SchoolUser resources){
        return new ResponseEntity<>(SchoolUserService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改school_user")
    @ApiOperation("修改school_user")
    @PreAuthorize("@el.check('SchoolUser:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody SchoolUser resources){
        SchoolUserService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除school_user")
    @ApiOperation("删除school_user")
    @PreAuthorize("@el.check('SchoolUser:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        SchoolUserService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}