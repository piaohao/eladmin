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
* @date 2020-06-04
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "CompanyFinancing管理")
@RequestMapping("/api/companyFinancing")
public class CompanyFinancingController {

    private final CompanyFinancingService companyFinancingService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('companyFinancing:list')")
    public void download(HttpServletResponse response, CompanyFinancingQueryCriteria criteria) throws IOException {
        companyFinancingService.download(companyFinancingService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询CompanyFinancing")
    @ApiOperation("查询CompanyFinancing")
    @PreAuthorize("@el.check('companyFinancing:list')")
    public ResponseEntity<Object> query(CompanyFinancingQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(companyFinancingService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增CompanyFinancing")
    @ApiOperation("新增CompanyFinancing")
    @PreAuthorize("@el.check('companyFinancing:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody CompanyFinancing resources){
        return new ResponseEntity<>(companyFinancingService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改CompanyFinancing")
    @ApiOperation("修改CompanyFinancing")
    @PreAuthorize("@el.check('companyFinancing:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody CompanyFinancing resources){
        companyFinancingService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除CompanyFinancing")
    @ApiOperation("删除CompanyFinancing")
    @PreAuthorize("@el.check('companyFinancing:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        companyFinancingService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}