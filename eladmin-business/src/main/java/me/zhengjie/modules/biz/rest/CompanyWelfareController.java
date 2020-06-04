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
import me.zhengjie.api.domain.biz.CompanyWelfare;
import me.zhengjie.modules.biz.service.CompanyWelfareService;
import me.zhengjie.modules.biz.service.dto.CompanyWelfareQueryCriteria;
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
@Api(tags = "CompanyWelfare管理")
@RequestMapping("/api/companyWelfare")
public class CompanyWelfareController {

    private final CompanyWelfareService companyWelfareService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('companyWelfare:list')")
    public void download(HttpServletResponse response, CompanyWelfareQueryCriteria criteria) throws IOException {
        companyWelfareService.download(companyWelfareService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询CompanyWelfare")
    @ApiOperation("查询CompanyWelfare")
    @PreAuthorize("@el.check('companyWelfare:list')")
    public ResponseEntity<Object> query(CompanyWelfareQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(companyWelfareService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增CompanyWelfare")
    @ApiOperation("新增CompanyWelfare")
    @PreAuthorize("@el.check('companyWelfare:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody CompanyWelfare resources){
        return new ResponseEntity<>(companyWelfareService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改CompanyWelfare")
    @ApiOperation("修改CompanyWelfare")
    @PreAuthorize("@el.check('companyWelfare:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody CompanyWelfare resources){
        companyWelfareService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除CompanyWelfare")
    @ApiOperation("删除CompanyWelfare")
    @PreAuthorize("@el.check('companyWelfare:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        companyWelfareService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}