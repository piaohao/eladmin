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
* @date 2020-06-04
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "ApprenticeProject管理")
@RequestMapping("/api/apprenticeProject")
public class ApprenticeProjectController {

    private final ApprenticeProjectService apprenticeProjectService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('apprenticeProject:list')")
    public void download(HttpServletResponse response, ApprenticeProjectQueryCriteria criteria) throws IOException {
        apprenticeProjectService.download(apprenticeProjectService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询ApprenticeProject")
    @ApiOperation("查询ApprenticeProject")
    @PreAuthorize("@el.check('apprenticeProject:list')")
    public ResponseEntity<Object> query(ApprenticeProjectQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(apprenticeProjectService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增ApprenticeProject")
    @ApiOperation("新增ApprenticeProject")
    @PreAuthorize("@el.check('apprenticeProject:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody ApprenticeProject resources){
        return new ResponseEntity<>(apprenticeProjectService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改ApprenticeProject")
    @ApiOperation("修改ApprenticeProject")
    @PreAuthorize("@el.check('apprenticeProject:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody ApprenticeProject resources){
        apprenticeProjectService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除ApprenticeProject")
    @ApiOperation("删除ApprenticeProject")
    @PreAuthorize("@el.check('apprenticeProject:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        apprenticeProjectService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}