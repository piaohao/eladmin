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
import me.zhengjie.api.domain.biz.FeedBack;
import me.zhengjie.modules.biz.service.FeedBackService;
import me.zhengjie.modules.biz.service.dto.FeedBackQueryCriteria;
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
@Api(tags = "FeedBack管理")
@RequestMapping("/api/feedBack")
public class FeedBackController {

    private final FeedBackService feedBackService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('feedBack:list')")
    public void download(HttpServletResponse response, FeedBackQueryCriteria criteria) throws IOException {
        feedBackService.download(feedBackService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询FeedBack")
    @ApiOperation("查询FeedBack")
    @PreAuthorize("@el.check('feedBack:list')")
    public ResponseEntity<Object> query(FeedBackQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(feedBackService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增FeedBack")
    @ApiOperation("新增FeedBack")
    @PreAuthorize("@el.check('feedBack:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody FeedBack resources){
        return new ResponseEntity<>(feedBackService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改FeedBack")
    @ApiOperation("修改FeedBack")
    @PreAuthorize("@el.check('feedBack:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody FeedBack resources){
        feedBackService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除FeedBack")
    @ApiOperation("删除FeedBack")
    @PreAuthorize("@el.check('feedBack:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        feedBackService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}