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
import me.zhengjie.api.domain.biz.CompanyScale;
import me.zhengjie.modules.biz.service.CompanyScaleService;
import me.zhengjie.modules.biz.service.dto.CompanyScaleQueryCriteria;
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
@Api(tags = "company_scale管理")
@RequestMapping("/api/CompanyScale")
public class CompanyScaleController {

    private final CompanyScaleService CompanyScaleService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('CompanyScale:list')")
    public void download(HttpServletResponse response, CompanyScaleQueryCriteria criteria) throws IOException {
        CompanyScaleService.download(CompanyScaleService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询company_scale")
    @ApiOperation("查询company_scale")
    @PreAuthorize("@el.check('CompanyScale:list')")
    public ResponseEntity<Object> query(CompanyScaleQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(CompanyScaleService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增company_scale")
    @ApiOperation("新增company_scale")
    @PreAuthorize("@el.check('CompanyScale:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody CompanyScale resources){
        return new ResponseEntity<>(CompanyScaleService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改company_scale")
    @ApiOperation("修改company_scale")
    @PreAuthorize("@el.check('CompanyScale:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody CompanyScale resources){
        CompanyScaleService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除company_scale")
    @ApiOperation("删除company_scale")
    @PreAuthorize("@el.check('CompanyScale:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        CompanyScaleService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}