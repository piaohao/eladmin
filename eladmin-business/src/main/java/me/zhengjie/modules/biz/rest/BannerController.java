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
import me.zhengjie.api.domain.biz.Banner;
import me.zhengjie.modules.biz.service.BannerService;
import me.zhengjie.modules.biz.service.dto.BannerQueryCriteria;
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
@Api(tags = "Banner管理")
@RequestMapping("/api/banner")
public class BannerController {

    private final BannerService bannerService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('banner:list')")
    public void download(HttpServletResponse response, BannerQueryCriteria criteria) throws IOException {
        bannerService.download(bannerService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询Banner")
    @ApiOperation("查询Banner")
    @PreAuthorize("@el.check('banner:list')")
    public ResponseEntity<Object> query(BannerQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(bannerService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增Banner")
    @ApiOperation("新增Banner")
    @PreAuthorize("@el.check('banner:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody Banner resources){
        return new ResponseEntity<>(bannerService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改Banner")
    @ApiOperation("修改Banner")
    @PreAuthorize("@el.check('banner:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody Banner resources){
        bannerService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除Banner")
    @ApiOperation("删除Banner")
    @PreAuthorize("@el.check('banner:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        bannerService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}