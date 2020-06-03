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
* @date 2020-06-03
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "company_message管理")
@RequestMapping("/api/CompanyMessage")
public class CompanyMessageController {

    private final CompanyMessageService CompanyMessageService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('CompanyMessage:list')")
    public void download(HttpServletResponse response, CompanyMessageQueryCriteria criteria) throws IOException {
        CompanyMessageService.download(CompanyMessageService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询company_message")
    @ApiOperation("查询company_message")
    @PreAuthorize("@el.check('CompanyMessage:list')")
    public ResponseEntity<Object> query(CompanyMessageQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(CompanyMessageService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增company_message")
    @ApiOperation("新增company_message")
    @PreAuthorize("@el.check('CompanyMessage:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody CompanyMessage resources){
        return new ResponseEntity<>(CompanyMessageService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改company_message")
    @ApiOperation("修改company_message")
    @PreAuthorize("@el.check('CompanyMessage:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody CompanyMessage resources){
        CompanyMessageService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除company_message")
    @ApiOperation("删除company_message")
    @PreAuthorize("@el.check('CompanyMessage:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        CompanyMessageService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}