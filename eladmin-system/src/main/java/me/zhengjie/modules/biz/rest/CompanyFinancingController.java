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
import me.zhengjie.api.domain.biz.CompanyFinancing;
import me.zhengjie.modules.biz.service.CompanyFinancingService;
import me.zhengjie.modules.biz.service.dto.CompanyFinancingQueryCriteria;
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
@Api(tags = "company_financing管理")
@RequestMapping("/api/CompanyFinancing")
public class CompanyFinancingController {

    private final CompanyFinancingService CompanyFinancingService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('CompanyFinancing:list')")
    public void download(HttpServletResponse response, CompanyFinancingQueryCriteria criteria) throws IOException {
        CompanyFinancingService.download(CompanyFinancingService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询company_financing")
    @ApiOperation("查询company_financing")
    @PreAuthorize("@el.check('CompanyFinancing:list')")
    public ResponseEntity<Object> query(CompanyFinancingQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(CompanyFinancingService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增company_financing")
    @ApiOperation("新增company_financing")
    @PreAuthorize("@el.check('CompanyFinancing:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody CompanyFinancing resources){
        return new ResponseEntity<>(CompanyFinancingService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改company_financing")
    @ApiOperation("修改company_financing")
    @PreAuthorize("@el.check('CompanyFinancing:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody CompanyFinancing resources){
        CompanyFinancingService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除company_financing")
    @ApiOperation("删除company_financing")
    @PreAuthorize("@el.check('CompanyFinancing:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        CompanyFinancingService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}