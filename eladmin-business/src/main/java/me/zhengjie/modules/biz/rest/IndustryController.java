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
import me.zhengjie.api.domain.biz.Industry;
import me.zhengjie.modules.biz.service.IndustryService;
import me.zhengjie.modules.biz.service.dto.IndustryQueryCriteria;
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
@Api(tags = "industry管理")
@RequestMapping("/api/Industry")
public class IndustryController {

    private final IndustryService IndustryService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('Industry:list')")
    public void download(HttpServletResponse response, IndustryQueryCriteria criteria) throws IOException {
        IndustryService.download(IndustryService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询industry")
    @ApiOperation("查询industry")
    @PreAuthorize("@el.check('Industry:list')")
    public ResponseEntity<Object> query(IndustryQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(IndustryService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增industry")
    @ApiOperation("新增industry")
    @PreAuthorize("@el.check('Industry:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody Industry resources){
        return new ResponseEntity<>(IndustryService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改industry")
    @ApiOperation("修改industry")
    @PreAuthorize("@el.check('Industry:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody Industry resources){
        IndustryService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除industry")
    @ApiOperation("删除industry")
    @PreAuthorize("@el.check('Industry:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        IndustryService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}