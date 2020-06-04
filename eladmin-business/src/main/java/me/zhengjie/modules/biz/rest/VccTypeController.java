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
import me.zhengjie.api.domain.biz.VccType;
import me.zhengjie.modules.biz.service.VccTypeService;
import me.zhengjie.modules.biz.service.dto.VccTypeQueryCriteria;
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
@Api(tags = "VccType管理")
@RequestMapping("/api/vccType")
public class VccTypeController {

    private final VccTypeService vccTypeService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('vccType:list')")
    public void download(HttpServletResponse response, VccTypeQueryCriteria criteria) throws IOException {
        vccTypeService.download(vccTypeService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询VccType")
    @ApiOperation("查询VccType")
    @PreAuthorize("@el.check('vccType:list')")
    public ResponseEntity<Object> query(VccTypeQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(vccTypeService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增VccType")
    @ApiOperation("新增VccType")
    @PreAuthorize("@el.check('vccType:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody VccType resources){
        return new ResponseEntity<>(vccTypeService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改VccType")
    @ApiOperation("修改VccType")
    @PreAuthorize("@el.check('vccType:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody VccType resources){
        vccTypeService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除VccType")
    @ApiOperation("删除VccType")
    @PreAuthorize("@el.check('vccType:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        vccTypeService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}