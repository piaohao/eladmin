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
import me.zhengjie.api.domain.biz.Interest;
import me.zhengjie.modules.biz.service.InterestService;
import me.zhengjie.modules.biz.service.dto.InterestQueryCriteria;
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
@Api(tags = "interest管理")
@RequestMapping("/api/Interest")
public class InterestController {

    private final InterestService InterestService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('Interest:list')")
    public void download(HttpServletResponse response, InterestQueryCriteria criteria) throws IOException {
        InterestService.download(InterestService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询interest")
    @ApiOperation("查询interest")
    @PreAuthorize("@el.check('Interest:list')")
    public ResponseEntity<Object> query(InterestQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(InterestService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增interest")
    @ApiOperation("新增interest")
    @PreAuthorize("@el.check('Interest:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody Interest resources){
        return new ResponseEntity<>(InterestService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改interest")
    @ApiOperation("修改interest")
    @PreAuthorize("@el.check('Interest:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody Interest resources){
        InterestService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除interest")
    @ApiOperation("删除interest")
    @PreAuthorize("@el.check('Interest:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        InterestService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}