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
import me.zhengjie.api.domain.biz.CompanyFollowing;
import me.zhengjie.modules.biz.service.CompanyFollowingService;
import me.zhengjie.modules.biz.service.dto.CompanyFollowingQueryCriteria;
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
@Api(tags = "company_following管理")
@RequestMapping("/api/CompanyFollowing")
public class CompanyFollowingController {

    private final CompanyFollowingService CompanyFollowingService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('CompanyFollowing:list')")
    public void download(HttpServletResponse response, CompanyFollowingQueryCriteria criteria) throws IOException {
        CompanyFollowingService.download(CompanyFollowingService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询company_following")
    @ApiOperation("查询company_following")
    @PreAuthorize("@el.check('CompanyFollowing:list')")
    public ResponseEntity<Object> query(CompanyFollowingQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(CompanyFollowingService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增company_following")
    @ApiOperation("新增company_following")
    @PreAuthorize("@el.check('CompanyFollowing:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody CompanyFollowing resources){
        return new ResponseEntity<>(CompanyFollowingService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改company_following")
    @ApiOperation("修改company_following")
    @PreAuthorize("@el.check('CompanyFollowing:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody CompanyFollowing resources){
        CompanyFollowingService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除company_following")
    @ApiOperation("删除company_following")
    @PreAuthorize("@el.check('CompanyFollowing:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        CompanyFollowingService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}