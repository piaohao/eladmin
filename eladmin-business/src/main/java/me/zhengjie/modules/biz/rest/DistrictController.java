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
import me.zhengjie.api.domain.biz.District;
import me.zhengjie.modules.biz.service.DistrictService;
import me.zhengjie.modules.biz.service.dto.DistrictQueryCriteria;
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
@Api(tags = "district管理")
@RequestMapping("/api/District")
public class DistrictController {

    private final DistrictService DistrictService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('District:list')")
    public void download(HttpServletResponse response, DistrictQueryCriteria criteria) throws IOException {
        DistrictService.download(DistrictService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询district")
    @ApiOperation("查询district")
    @PreAuthorize("@el.check('District:list')")
    public ResponseEntity<Object> query(DistrictQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(DistrictService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增district")
    @ApiOperation("新增district")
    @PreAuthorize("@el.check('District:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody District resources){
        return new ResponseEntity<>(DistrictService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改district")
    @ApiOperation("修改district")
    @PreAuthorize("@el.check('District:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody District resources){
        DistrictService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除district")
    @ApiOperation("删除district")
    @PreAuthorize("@el.check('District:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        DistrictService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}