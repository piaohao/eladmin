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
import me.zhengjie.api.domain.biz.Company;
import me.zhengjie.modules.biz.service.CompanyService;
import me.zhengjie.modules.biz.service.dto.CompanyQueryCriteria;
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
@Api(tags = "Company管理")
@RequestMapping("/api/company")
public class CompanyController {

    private final CompanyService companyService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('company:list')")
    public void download(HttpServletResponse response, CompanyQueryCriteria criteria) throws IOException {
        companyService.download(companyService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询Company")
    @ApiOperation("查询Company")
    @PreAuthorize("@el.check('company:list')")
    public ResponseEntity<Object> query(CompanyQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(companyService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增Company")
    @ApiOperation("新增Company")
    @PreAuthorize("@el.check('company:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody Company resources){
        return new ResponseEntity<>(companyService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改Company")
    @ApiOperation("修改Company")
    @PreAuthorize("@el.check('company:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody Company resources){
        companyService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除Company")
    @ApiOperation("删除Company")
    @PreAuthorize("@el.check('company:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        companyService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}