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
import me.zhengjie.api.domain.biz.VccApplicant;
import me.zhengjie.modules.biz.service.VccApplicantService;
import me.zhengjie.modules.biz.service.dto.VccApplicantQueryCriteria;
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
@Api(tags = "VccApplicant管理")
@RequestMapping("/api/vccApplicant")
public class VccApplicantController {

    private final VccApplicantService vccApplicantService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('vccApplicant:list')")
    public void download(HttpServletResponse response, VccApplicantQueryCriteria criteria) throws IOException {
        vccApplicantService.download(vccApplicantService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询VccApplicant")
    @ApiOperation("查询VccApplicant")
    @PreAuthorize("@el.check('vccApplicant:list')")
    public ResponseEntity<Object> query(VccApplicantQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(vccApplicantService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增VccApplicant")
    @ApiOperation("新增VccApplicant")
    @PreAuthorize("@el.check('vccApplicant:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody VccApplicant resources){
        return new ResponseEntity<>(vccApplicantService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改VccApplicant")
    @ApiOperation("修改VccApplicant")
    @PreAuthorize("@el.check('vccApplicant:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody VccApplicant resources){
        vccApplicantService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除VccApplicant")
    @ApiOperation("删除VccApplicant")
    @PreAuthorize("@el.check('vccApplicant:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        vccApplicantService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}