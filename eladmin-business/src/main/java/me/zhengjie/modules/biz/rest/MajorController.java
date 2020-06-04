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
import me.zhengjie.api.domain.biz.Major;
import me.zhengjie.modules.biz.service.MajorService;
import me.zhengjie.modules.biz.service.dto.MajorQueryCriteria;
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
@Api(tags = "Major管理")
@RequestMapping("/api/major")
public class MajorController {

    private final MajorService majorService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('major:list')")
    public void download(HttpServletResponse response, MajorQueryCriteria criteria) throws IOException {
        majorService.download(majorService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询Major")
    @ApiOperation("查询Major")
    @PreAuthorize("@el.check('major:list')")
    public ResponseEntity<Object> query(MajorQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(majorService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增Major")
    @ApiOperation("新增Major")
    @PreAuthorize("@el.check('major:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody Major resources){
        return new ResponseEntity<>(majorService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改Major")
    @ApiOperation("修改Major")
    @PreAuthorize("@el.check('major:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody Major resources){
        majorService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除Major")
    @ApiOperation("删除Major")
    @PreAuthorize("@el.check('major:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        majorService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}