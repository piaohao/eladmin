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
import me.zhengjie.api.domain.biz.Major;
import me.zhengjie.modules.biz.service.MajorService;
import me.zhengjie.modules.biz.service.dto.MajorQueryCriteria;
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
@Api(tags = "major管理")
@RequestMapping("/api/Major")
public class MajorController {

    private final MajorService MajorService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('Major:list')")
    public void download(HttpServletResponse response, MajorQueryCriteria criteria) throws IOException {
        MajorService.download(MajorService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询major")
    @ApiOperation("查询major")
    @PreAuthorize("@el.check('Major:list')")
    public ResponseEntity<Object> query(MajorQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(MajorService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增major")
    @ApiOperation("新增major")
    @PreAuthorize("@el.check('Major:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody Major resources){
        return new ResponseEntity<>(MajorService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改major")
    @ApiOperation("修改major")
    @PreAuthorize("@el.check('Major:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody Major resources){
        MajorService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除major")
    @ApiOperation("删除major")
    @PreAuthorize("@el.check('Major:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        MajorService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}