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
import me.zhengjie.api.domain.biz.FeedBack;
import me.zhengjie.modules.biz.service.FeedBackService;
import me.zhengjie.modules.biz.service.dto.FeedBackQueryCriteria;
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
@Api(tags = "feed_back管理")
@RequestMapping("/api/FeedBack")
public class FeedBackController {

    private final FeedBackService FeedBackService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('FeedBack:list')")
    public void download(HttpServletResponse response, FeedBackQueryCriteria criteria) throws IOException {
        FeedBackService.download(FeedBackService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询feed_back")
    @ApiOperation("查询feed_back")
    @PreAuthorize("@el.check('FeedBack:list')")
    public ResponseEntity<Object> query(FeedBackQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(FeedBackService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增feed_back")
    @ApiOperation("新增feed_back")
    @PreAuthorize("@el.check('FeedBack:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody FeedBack resources){
        return new ResponseEntity<>(FeedBackService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改feed_back")
    @ApiOperation("修改feed_back")
    @PreAuthorize("@el.check('FeedBack:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody FeedBack resources){
        FeedBackService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除feed_back")
    @ApiOperation("删除feed_back")
    @PreAuthorize("@el.check('FeedBack:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        FeedBackService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}