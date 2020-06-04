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
import me.zhengjie.api.domain.biz.SalaryType;
import me.zhengjie.modules.biz.service.SalaryTypeService;
import me.zhengjie.modules.biz.service.dto.SalaryTypeQueryCriteria;
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
@Api(tags = "SalaryType管理")
@RequestMapping("/api/salaryType")
public class SalaryTypeController {

    private final SalaryTypeService salaryTypeService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('salaryType:list')")
    public void download(HttpServletResponse response, SalaryTypeQueryCriteria criteria) throws IOException {
        salaryTypeService.download(salaryTypeService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询SalaryType")
    @ApiOperation("查询SalaryType")
    @PreAuthorize("@el.check('salaryType:list')")
    public ResponseEntity<Object> query(SalaryTypeQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(salaryTypeService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增SalaryType")
    @ApiOperation("新增SalaryType")
    @PreAuthorize("@el.check('salaryType:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody SalaryType resources){
        return new ResponseEntity<>(salaryTypeService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改SalaryType")
    @ApiOperation("修改SalaryType")
    @PreAuthorize("@el.check('salaryType:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody SalaryType resources){
        salaryTypeService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除SalaryType")
    @ApiOperation("删除SalaryType")
    @PreAuthorize("@el.check('salaryType:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        salaryTypeService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}