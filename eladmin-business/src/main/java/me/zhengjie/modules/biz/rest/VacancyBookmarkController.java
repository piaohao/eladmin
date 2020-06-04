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
* @date 2020-06-04
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "VacancyBookmark管理")
@RequestMapping("/api/vacancyBookmark")
public class VacancyBookmarkController {

    private final VacancyBookmarkService vacancyBookmarkService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('vacancyBookmark:list')")
    public void download(HttpServletResponse response, VacancyBookmarkQueryCriteria criteria) throws IOException {
        vacancyBookmarkService.download(vacancyBookmarkService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询VacancyBookmark")
    @ApiOperation("查询VacancyBookmark")
    @PreAuthorize("@el.check('vacancyBookmark:list')")
    public ResponseEntity<Object> query(VacancyBookmarkQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(vacancyBookmarkService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增VacancyBookmark")
    @ApiOperation("新增VacancyBookmark")
    @PreAuthorize("@el.check('vacancyBookmark:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody VacancyBookmark resources){
        return new ResponseEntity<>(vacancyBookmarkService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改VacancyBookmark")
    @ApiOperation("修改VacancyBookmark")
    @PreAuthorize("@el.check('vacancyBookmark:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody VacancyBookmark resources){
        vacancyBookmarkService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除VacancyBookmark")
    @ApiOperation("删除VacancyBookmark")
    @PreAuthorize("@el.check('vacancyBookmark:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        vacancyBookmarkService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}