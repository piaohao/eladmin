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
import me.zhengjie.api.domain.biz.CompanyUser;
import me.zhengjie.modules.biz.service.CompanyUserService;
import me.zhengjie.modules.biz.service.dto.CompanyUserQueryCriteria;
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
@Api(tags = "CompanyUser管理")
@RequestMapping("/api/companyUser")
public class CompanyUserController {

    private final CompanyUserService companyUserService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('companyUser:list')")
    public void download(HttpServletResponse response, CompanyUserQueryCriteria criteria) throws IOException {
        companyUserService.download(companyUserService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询CompanyUser")
    @ApiOperation("查询CompanyUser")
    @PreAuthorize("@el.check('companyUser:list')")
    public ResponseEntity<Object> query(CompanyUserQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(companyUserService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增CompanyUser")
    @ApiOperation("新增CompanyUser")
    @PreAuthorize("@el.check('companyUser:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody CompanyUser resources){
        return new ResponseEntity<>(companyUserService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改CompanyUser")
    @ApiOperation("修改CompanyUser")
    @PreAuthorize("@el.check('companyUser:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody CompanyUser resources){
        companyUserService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除CompanyUser")
    @ApiOperation("删除CompanyUser")
    @PreAuthorize("@el.check('companyUser:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        companyUserService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}