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
import me.zhengjie.api.domain.biz.District;
import me.zhengjie.modules.biz.service.DistrictService;
import me.zhengjie.modules.biz.service.dto.DistrictQueryCriteria;
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
@Api(tags = "District管理")
@RequestMapping("/api/district")
public class DistrictController {

    private final DistrictService districtService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('district:list')")
    public void download(HttpServletResponse response, DistrictQueryCriteria criteria) throws IOException {
        districtService.download(districtService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询District")
    @ApiOperation("查询District")
    @PreAuthorize("@el.check('district:list')")
    public ResponseEntity<Object> query(DistrictQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(districtService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增District")
    @ApiOperation("新增District")
    @PreAuthorize("@el.check('district:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody District resources){
        return new ResponseEntity<>(districtService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改District")
    @ApiOperation("修改District")
    @PreAuthorize("@el.check('district:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody District resources){
        districtService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除District")
    @ApiOperation("删除District")
    @PreAuthorize("@el.check('district:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        districtService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}