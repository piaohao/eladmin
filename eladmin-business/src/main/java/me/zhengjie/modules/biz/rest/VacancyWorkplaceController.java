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
import me.zhengjie.api.domain.biz.VacancyWorkplace;
import me.zhengjie.modules.biz.service.VacancyWorkplaceService;
import me.zhengjie.modules.biz.service.dto.VacancyWorkplaceQueryCriteria;
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
@Api(tags = "VacancyWorkplace管理")
@RequestMapping("/api/vacancyWorkplace")
public class VacancyWorkplaceController {

    private final VacancyWorkplaceService vacancyWorkplaceService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('vacancyWorkplace:list')")
    public void download(HttpServletResponse response, VacancyWorkplaceQueryCriteria criteria) throws IOException {
        vacancyWorkplaceService.download(vacancyWorkplaceService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询VacancyWorkplace")
    @ApiOperation("查询VacancyWorkplace")
    @PreAuthorize("@el.check('vacancyWorkplace:list')")
    public ResponseEntity<Object> query(VacancyWorkplaceQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(vacancyWorkplaceService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增VacancyWorkplace")
    @ApiOperation("新增VacancyWorkplace")
    @PreAuthorize("@el.check('vacancyWorkplace:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody VacancyWorkplace resources){
        return new ResponseEntity<>(vacancyWorkplaceService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改VacancyWorkplace")
    @ApiOperation("修改VacancyWorkplace")
    @PreAuthorize("@el.check('vacancyWorkplace:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody VacancyWorkplace resources){
        vacancyWorkplaceService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除VacancyWorkplace")
    @ApiOperation("删除VacancyWorkplace")
    @PreAuthorize("@el.check('vacancyWorkplace:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        vacancyWorkplaceService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}