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
import me.zhengjie.api.domain.biz.Mentor;
import me.zhengjie.modules.biz.service.MentorService;
import me.zhengjie.modules.biz.service.dto.MentorQueryCriteria;
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
@Api(tags = "Mentor管理")
@RequestMapping("/api/mentor")
public class MentorController {

    private final MentorService mentorService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('mentor:list')")
    public void download(HttpServletResponse response, MentorQueryCriteria criteria) throws IOException {
        mentorService.download(mentorService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询Mentor")
    @ApiOperation("查询Mentor")
    @PreAuthorize("@el.check('mentor:list')")
    public ResponseEntity<Object> query(MentorQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(mentorService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增Mentor")
    @ApiOperation("新增Mentor")
    @PreAuthorize("@el.check('mentor:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody Mentor resources){
        return new ResponseEntity<>(mentorService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改Mentor")
    @ApiOperation("修改Mentor")
    @PreAuthorize("@el.check('mentor:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody Mentor resources){
        mentorService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除Mentor")
    @ApiOperation("删除Mentor")
    @PreAuthorize("@el.check('mentor:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        mentorService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}