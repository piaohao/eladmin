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
import me.zhengjie.api.domain.biz.College;
import me.zhengjie.modules.biz.service.CollegeService;
import me.zhengjie.modules.biz.service.dto.CollegeQueryCriteria;
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
@Api(tags = "college管理")
@RequestMapping("/api/College")
public class CollegeController {

    private final CollegeService CollegeService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('College:list')")
    public void download(HttpServletResponse response, CollegeQueryCriteria criteria) throws IOException {
        CollegeService.download(CollegeService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询college")
    @ApiOperation("查询college")
    @PreAuthorize("@el.check('College:list')")
    public ResponseEntity<Object> query(CollegeQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(CollegeService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增college")
    @ApiOperation("新增college")
    @PreAuthorize("@el.check('College:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody College resources){
        return new ResponseEntity<>(CollegeService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改college")
    @ApiOperation("修改college")
    @PreAuthorize("@el.check('College:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody College resources){
        CollegeService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除college")
    @ApiOperation("删除college")
    @PreAuthorize("@el.check('College:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        CollegeService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}