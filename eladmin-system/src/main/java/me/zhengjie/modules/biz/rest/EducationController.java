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
import me.zhengjie.api.domain.biz.Education;
import me.zhengjie.modules.biz.service.EducationService;
import me.zhengjie.modules.biz.service.dto.EducationQueryCriteria;
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
@Api(tags = "education管理")
@RequestMapping("/api/Education")
public class EducationController {

    private final EducationService EducationService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('Education:list')")
    public void download(HttpServletResponse response, EducationQueryCriteria criteria) throws IOException {
        EducationService.download(EducationService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询education")
    @ApiOperation("查询education")
    @PreAuthorize("@el.check('Education:list')")
    public ResponseEntity<Object> query(EducationQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(EducationService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增education")
    @ApiOperation("新增education")
    @PreAuthorize("@el.check('Education:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody Education resources){
        return new ResponseEntity<>(EducationService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改education")
    @ApiOperation("修改education")
    @PreAuthorize("@el.check('Education:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody Education resources){
        EducationService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除education")
    @ApiOperation("删除education")
    @PreAuthorize("@el.check('Education:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        EducationService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}