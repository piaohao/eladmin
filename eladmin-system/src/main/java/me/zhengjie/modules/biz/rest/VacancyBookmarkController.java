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
import me.zhengjie.api.domain.biz.VacancyBookmark;
import me.zhengjie.modules.biz.service.VacancyBookmarkService;
import me.zhengjie.modules.biz.service.dto.VacancyBookmarkQueryCriteria;
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
@Api(tags = "vacancy_bookmark管理")
@RequestMapping("/api/VacancyBookmark")
public class VacancyBookmarkController {

    private final VacancyBookmarkService VacancyBookmarkService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('VacancyBookmark:list')")
    public void download(HttpServletResponse response, VacancyBookmarkQueryCriteria criteria) throws IOException {
        VacancyBookmarkService.download(VacancyBookmarkService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询vacancy_bookmark")
    @ApiOperation("查询vacancy_bookmark")
    @PreAuthorize("@el.check('VacancyBookmark:list')")
    public ResponseEntity<Object> query(VacancyBookmarkQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(VacancyBookmarkService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增vacancy_bookmark")
    @ApiOperation("新增vacancy_bookmark")
    @PreAuthorize("@el.check('VacancyBookmark:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody VacancyBookmark resources){
        return new ResponseEntity<>(VacancyBookmarkService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改vacancy_bookmark")
    @ApiOperation("修改vacancy_bookmark")
    @PreAuthorize("@el.check('VacancyBookmark:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody VacancyBookmark resources){
        VacancyBookmarkService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除vacancy_bookmark")
    @ApiOperation("删除vacancy_bookmark")
    @PreAuthorize("@el.check('VacancyBookmark:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        VacancyBookmarkService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}