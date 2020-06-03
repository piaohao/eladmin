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
import me.zhengjie.api.domain.biz.StudentUser;
import me.zhengjie.modules.biz.service.StudentUserService;
import me.zhengjie.modules.biz.service.dto.StudentUserQueryCriteria;
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
@Api(tags = "student_user管理")
@RequestMapping("/api/StudentUser")
public class StudentUserController {

    private final StudentUserService StudentUserService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('StudentUser:list')")
    public void download(HttpServletResponse response, StudentUserQueryCriteria criteria) throws IOException {
        StudentUserService.download(StudentUserService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询student_user")
    @ApiOperation("查询student_user")
    @PreAuthorize("@el.check('StudentUser:list')")
    public ResponseEntity<Object> query(StudentUserQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(StudentUserService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增student_user")
    @ApiOperation("新增student_user")
    @PreAuthorize("@el.check('StudentUser:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody StudentUser resources){
        return new ResponseEntity<>(StudentUserService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改student_user")
    @ApiOperation("修改student_user")
    @PreAuthorize("@el.check('StudentUser:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody StudentUser resources){
        StudentUserService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除student_user")
    @ApiOperation("删除student_user")
    @PreAuthorize("@el.check('StudentUser:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        StudentUserService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}