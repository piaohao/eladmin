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
import me.zhengjie.api.domain.biz.CompanyMessage;
import me.zhengjie.modules.biz.service.CompanyMessageService;
import me.zhengjie.modules.biz.service.dto.CompanyMessageQueryCriteria;
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
@Api(tags = "CompanyMessage管理")
@RequestMapping("/api/companyMessage")
public class CompanyMessageController {

    private final CompanyMessageService companyMessageService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('companyMessage:list')")
    public void download(HttpServletResponse response, CompanyMessageQueryCriteria criteria) throws IOException {
        companyMessageService.download(companyMessageService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询CompanyMessage")
    @ApiOperation("查询CompanyMessage")
    @PreAuthorize("@el.check('companyMessage:list')")
    public ResponseEntity<Object> query(CompanyMessageQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(companyMessageService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增CompanyMessage")
    @ApiOperation("新增CompanyMessage")
    @PreAuthorize("@el.check('companyMessage:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody CompanyMessage resources){
        return new ResponseEntity<>(companyMessageService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改CompanyMessage")
    @ApiOperation("修改CompanyMessage")
    @PreAuthorize("@el.check('companyMessage:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody CompanyMessage resources){
        companyMessageService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除CompanyMessage")
    @ApiOperation("删除CompanyMessage")
    @PreAuthorize("@el.check('companyMessage:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        companyMessageService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}