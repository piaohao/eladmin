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
import me.zhengjie.api.domain.biz.ResumePrize;
import me.zhengjie.modules.biz.service.ResumePrizeService;
import me.zhengjie.modules.biz.service.dto.ResumePrizeQueryCriteria;
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
@Api(tags = "ResumePrize管理")
@RequestMapping("/api/resumePrize")
public class ResumePrizeController {

    private final ResumePrizeService resumePrizeService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('resumePrize:list')")
    public void download(HttpServletResponse response, ResumePrizeQueryCriteria criteria) throws IOException {
        resumePrizeService.download(resumePrizeService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询ResumePrize")
    @ApiOperation("查询ResumePrize")
    @PreAuthorize("@el.check('resumePrize:list')")
    public ResponseEntity<Object> query(ResumePrizeQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(resumePrizeService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增ResumePrize")
    @ApiOperation("新增ResumePrize")
    @PreAuthorize("@el.check('resumePrize:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody ResumePrize resources){
        return new ResponseEntity<>(resumePrizeService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改ResumePrize")
    @ApiOperation("修改ResumePrize")
    @PreAuthorize("@el.check('resumePrize:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody ResumePrize resources){
        resumePrizeService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除ResumePrize")
    @ApiOperation("删除ResumePrize")
    @PreAuthorize("@el.check('resumePrize:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        resumePrizeService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}