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
import me.zhengjie.api.domain.biz.Vacancy;
import me.zhengjie.modules.biz.service.VacancyService;
import me.zhengjie.modules.biz.service.dto.VacancyQueryCriteria;
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
@Api(tags = "vacancy管理")
@RequestMapping("/api/Vacancy")
public class VacancyController {

    private final VacancyService VacancyService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('Vacancy:list')")
    public void download(HttpServletResponse response, VacancyQueryCriteria criteria) throws IOException {
        VacancyService.download(VacancyService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询vacancy")
    @ApiOperation("查询vacancy")
    @PreAuthorize("@el.check('Vacancy:list')")
    public ResponseEntity<Object> query(VacancyQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(VacancyService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增vacancy")
    @ApiOperation("新增vacancy")
    @PreAuthorize("@el.check('Vacancy:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody Vacancy resources){
        return new ResponseEntity<>(VacancyService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改vacancy")
    @ApiOperation("修改vacancy")
    @PreAuthorize("@el.check('Vacancy:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody Vacancy resources){
        VacancyService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除vacancy")
    @ApiOperation("删除vacancy")
    @PreAuthorize("@el.check('Vacancy:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        VacancyService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}