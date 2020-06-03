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
import me.zhengjie.api.domain.biz.Mentor;
import me.zhengjie.modules.biz.service.MentorService;
import me.zhengjie.modules.biz.service.dto.MentorQueryCriteria;
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
@Api(tags = "mentor管理")
@RequestMapping("/api/Mentor")
public class MentorController {

    private final MentorService MentorService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('Mentor:list')")
    public void download(HttpServletResponse response, MentorQueryCriteria criteria) throws IOException {
        MentorService.download(MentorService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询mentor")
    @ApiOperation("查询mentor")
    @PreAuthorize("@el.check('Mentor:list')")
    public ResponseEntity<Object> query(MentorQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(MentorService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增mentor")
    @ApiOperation("新增mentor")
    @PreAuthorize("@el.check('Mentor:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody Mentor resources){
        return new ResponseEntity<>(MentorService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改mentor")
    @ApiOperation("修改mentor")
    @PreAuthorize("@el.check('Mentor:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody Mentor resources){
        MentorService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除mentor")
    @ApiOperation("删除mentor")
    @PreAuthorize("@el.check('Mentor:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        MentorService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}