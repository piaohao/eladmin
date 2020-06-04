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
import me.zhengjie.api.domain.biz.Industry;
import me.zhengjie.modules.biz.service.IndustryService;
import me.zhengjie.modules.biz.service.dto.IndustryQueryCriteria;
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
@Api(tags = "Industry管理")
@RequestMapping("/api/industry")
public class IndustryController {

    private final IndustryService industryService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('industry:list')")
    public void download(HttpServletResponse response, IndustryQueryCriteria criteria) throws IOException {
        industryService.download(industryService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询Industry")
    @ApiOperation("查询Industry")
    @PreAuthorize("@el.check('industry:list')")
    public ResponseEntity<Object> query(IndustryQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(industryService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增Industry")
    @ApiOperation("新增Industry")
    @PreAuthorize("@el.check('industry:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody Industry resources){
        return new ResponseEntity<>(industryService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改Industry")
    @ApiOperation("修改Industry")
    @PreAuthorize("@el.check('industry:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody Industry resources){
        industryService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除Industry")
    @ApiOperation("删除Industry")
    @PreAuthorize("@el.check('industry:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        industryService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}