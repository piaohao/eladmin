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
import me.zhengjie.api.domain.biz.VccApplicant;
import me.zhengjie.modules.biz.service.VccApplicantService;
import me.zhengjie.modules.biz.service.dto.VccApplicantQueryCriteria;
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
@Api(tags = "vcc_applicant管理")
@RequestMapping("/api/VccApplicant")
public class VccApplicantController {

    private final VccApplicantService VccApplicantService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('VccApplicant:list')")
    public void download(HttpServletResponse response, VccApplicantQueryCriteria criteria) throws IOException {
        VccApplicantService.download(VccApplicantService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询vcc_applicant")
    @ApiOperation("查询vcc_applicant")
    @PreAuthorize("@el.check('VccApplicant:list')")
    public ResponseEntity<Object> query(VccApplicantQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(VccApplicantService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增vcc_applicant")
    @ApiOperation("新增vcc_applicant")
    @PreAuthorize("@el.check('VccApplicant:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody VccApplicant resources){
        return new ResponseEntity<>(VccApplicantService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改vcc_applicant")
    @ApiOperation("修改vcc_applicant")
    @PreAuthorize("@el.check('VccApplicant:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody VccApplicant resources){
        VccApplicantService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除vcc_applicant")
    @ApiOperation("删除vcc_applicant")
    @PreAuthorize("@el.check('VccApplicant:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        VccApplicantService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}