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
import me.zhengjie.api.domain.biz.AdminUser;
import me.zhengjie.modules.biz.service.AdminUserService;
import me.zhengjie.modules.biz.service.dto.AdminUserQueryCriteria;
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
@Api(tags = "admin_user管理")
@RequestMapping("/api/AdminUser")
public class AdminUserController {

    private final AdminUserService AdminUserService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('AdminUser:list')")
    public void download(HttpServletResponse response, AdminUserQueryCriteria criteria) throws IOException {
        AdminUserService.download(AdminUserService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询admin_user")
    @ApiOperation("查询admin_user")
    @PreAuthorize("@el.check('AdminUser:list')")
    public ResponseEntity<Object> query(AdminUserQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(AdminUserService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增admin_user")
    @ApiOperation("新增admin_user")
    @PreAuthorize("@el.check('AdminUser:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody AdminUser resources){
        return new ResponseEntity<>(AdminUserService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改admin_user")
    @ApiOperation("修改admin_user")
    @PreAuthorize("@el.check('AdminUser:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody AdminUser resources){
        AdminUserService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除admin_user")
    @ApiOperation("删除admin_user")
    @PreAuthorize("@el.check('AdminUser:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        AdminUserService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}