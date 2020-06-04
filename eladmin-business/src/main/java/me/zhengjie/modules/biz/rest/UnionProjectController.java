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
import me.zhengjie.api.domain.biz.UnionProject;
import me.zhengjie.modules.biz.service.UnionProjectService;
import me.zhengjie.modules.biz.service.dto.UnionProjectQueryCriteria;
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
@Api(tags = "UnionProject管理")
@RequestMapping("/api/unionProject")
public class UnionProjectController {

    private final UnionProjectService unionProjectService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('unionProject:list')")
    public void download(HttpServletResponse response, UnionProjectQueryCriteria criteria) throws IOException {
        unionProjectService.download(unionProjectService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询UnionProject")
    @ApiOperation("查询UnionProject")
    @PreAuthorize("@el.check('unionProject:list')")
    public ResponseEntity<Object> query(UnionProjectQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(unionProjectService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增UnionProject")
    @ApiOperation("新增UnionProject")
    @PreAuthorize("@el.check('unionProject:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody UnionProject resources){
        return new ResponseEntity<>(unionProjectService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改UnionProject")
    @ApiOperation("修改UnionProject")
    @PreAuthorize("@el.check('unionProject:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody UnionProject resources){
        unionProjectService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除UnionProject")
    @ApiOperation("删除UnionProject")
    @PreAuthorize("@el.check('unionProject:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        unionProjectService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}