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
import me.zhengjie.api.domain.biz.CompanyWelfare;
import me.zhengjie.modules.biz.service.CompanyWelfareService;
import me.zhengjie.modules.biz.service.dto.CompanyWelfareQueryCriteria;
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
@Api(tags = "company_welfare管理")
@RequestMapping("/api/CompanyWelfare")
public class CompanyWelfareController {

    private final CompanyWelfareService CompanyWelfareService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('CompanyWelfare:list')")
    public void download(HttpServletResponse response, CompanyWelfareQueryCriteria criteria) throws IOException {
        CompanyWelfareService.download(CompanyWelfareService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询company_welfare")
    @ApiOperation("查询company_welfare")
    @PreAuthorize("@el.check('CompanyWelfare:list')")
    public ResponseEntity<Object> query(CompanyWelfareQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(CompanyWelfareService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增company_welfare")
    @ApiOperation("新增company_welfare")
    @PreAuthorize("@el.check('CompanyWelfare:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody CompanyWelfare resources){
        return new ResponseEntity<>(CompanyWelfareService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改company_welfare")
    @ApiOperation("修改company_welfare")
    @PreAuthorize("@el.check('CompanyWelfare:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody CompanyWelfare resources){
        CompanyWelfareService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除company_welfare")
    @ApiOperation("删除company_welfare")
    @PreAuthorize("@el.check('CompanyWelfare:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        CompanyWelfareService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}