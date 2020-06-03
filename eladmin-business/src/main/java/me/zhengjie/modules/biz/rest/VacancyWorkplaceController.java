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
import me.zhengjie.api.domain.biz.VacancyWorkplace;
import me.zhengjie.modules.biz.service.VacancyWorkplaceService;
import me.zhengjie.modules.biz.service.dto.VacancyWorkplaceQueryCriteria;
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
@Api(tags = "vacancy_workplace管理")
@RequestMapping("/api/VacancyWorkplace")
public class VacancyWorkplaceController {

    private final VacancyWorkplaceService VacancyWorkplaceService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('VacancyWorkplace:list')")
    public void download(HttpServletResponse response, VacancyWorkplaceQueryCriteria criteria) throws IOException {
        VacancyWorkplaceService.download(VacancyWorkplaceService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询vacancy_workplace")
    @ApiOperation("查询vacancy_workplace")
    @PreAuthorize("@el.check('VacancyWorkplace:list')")
    public ResponseEntity<Object> query(VacancyWorkplaceQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(VacancyWorkplaceService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增vacancy_workplace")
    @ApiOperation("新增vacancy_workplace")
    @PreAuthorize("@el.check('VacancyWorkplace:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody VacancyWorkplace resources){
        return new ResponseEntity<>(VacancyWorkplaceService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改vacancy_workplace")
    @ApiOperation("修改vacancy_workplace")
    @PreAuthorize("@el.check('VacancyWorkplace:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody VacancyWorkplace resources){
        VacancyWorkplaceService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除vacancy_workplace")
    @ApiOperation("删除vacancy_workplace")
    @PreAuthorize("@el.check('VacancyWorkplace:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        VacancyWorkplaceService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}