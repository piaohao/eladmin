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
import me.zhengjie.api.domain.biz.City;
import me.zhengjie.modules.biz.service.CityService;
import me.zhengjie.modules.biz.service.dto.CityQueryCriteria;
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
@Api(tags = "city管理")
@RequestMapping("/api/City")
public class CityController {

    private final CityService CityService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('City:list')")
    public void download(HttpServletResponse response, CityQueryCriteria criteria) throws IOException {
        CityService.download(CityService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询city")
    @ApiOperation("查询city")
    @PreAuthorize("@el.check('City:list')")
    public ResponseEntity<Object> query(CityQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(CityService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增city")
    @ApiOperation("新增city")
    @PreAuthorize("@el.check('City:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody City resources){
        return new ResponseEntity<>(CityService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改city")
    @ApiOperation("修改city")
    @PreAuthorize("@el.check('City:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody City resources){
        CityService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除city")
    @ApiOperation("删除city")
    @PreAuthorize("@el.check('City:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        CityService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}