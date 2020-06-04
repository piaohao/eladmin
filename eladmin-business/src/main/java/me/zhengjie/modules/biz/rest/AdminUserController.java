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
import me.zhengjie.api.domain.biz.AdminUser;
import me.zhengjie.modules.biz.service.AdminUserService;
import me.zhengjie.modules.biz.service.dto.AdminUserQueryCriteria;
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
@Api(tags = "AdminUser管理")
@RequestMapping("/api/adminUser")
public class AdminUserController {

    private final AdminUserService adminUserService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('adminUser:list')")
    public void download(HttpServletResponse response, AdminUserQueryCriteria criteria) throws IOException {
        adminUserService.download(adminUserService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询AdminUser")
    @ApiOperation("查询AdminUser")
    @PreAuthorize("@el.check('adminUser:list')")
    public ResponseEntity<Object> query(AdminUserQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(adminUserService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增AdminUser")
    @ApiOperation("新增AdminUser")
    @PreAuthorize("@el.check('adminUser:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody AdminUser resources){
        return new ResponseEntity<>(adminUserService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改AdminUser")
    @ApiOperation("修改AdminUser")
    @PreAuthorize("@el.check('adminUser:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody AdminUser resources){
        adminUserService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除AdminUser")
    @ApiOperation("删除AdminUser")
    @PreAuthorize("@el.check('adminUser:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        adminUserService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}