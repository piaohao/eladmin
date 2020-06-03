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
import me.zhengjie.api.domain.biz.ApprenticeProject;
import me.zhengjie.modules.biz.service.ApprenticeProjectService;
import me.zhengjie.modules.biz.service.dto.ApprenticeProjectQueryCriteria;
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
@Api(tags = "apprentice_project管理")
@RequestMapping("/api/ApprenticeProject")
public class ApprenticeProjectController {

    private final ApprenticeProjectService ApprenticeProjectService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('ApprenticeProject:list')")
    public void download(HttpServletResponse response, ApprenticeProjectQueryCriteria criteria) throws IOException {
        ApprenticeProjectService.download(ApprenticeProjectService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询apprentice_project")
    @ApiOperation("查询apprentice_project")
    @PreAuthorize("@el.check('ApprenticeProject:list')")
    public ResponseEntity<Object> query(ApprenticeProjectQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(ApprenticeProjectService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增apprentice_project")
    @ApiOperation("新增apprentice_project")
    @PreAuthorize("@el.check('ApprenticeProject:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody ApprenticeProject resources){
        return new ResponseEntity<>(ApprenticeProjectService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改apprentice_project")
    @ApiOperation("修改apprentice_project")
    @PreAuthorize("@el.check('ApprenticeProject:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody ApprenticeProject resources){
        ApprenticeProjectService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除apprentice_project")
    @ApiOperation("删除apprentice_project")
    @PreAuthorize("@el.check('ApprenticeProject:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        ApprenticeProjectService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}