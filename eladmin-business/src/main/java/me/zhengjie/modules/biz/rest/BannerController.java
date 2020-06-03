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
import me.zhengjie.api.domain.biz.Banner;
import me.zhengjie.modules.biz.service.BannerService;
import me.zhengjie.modules.biz.service.dto.BannerQueryCriteria;
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
@Api(tags = "banner管理")
@RequestMapping("/api/Banner")
public class BannerController {

    private final BannerService BannerService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('Banner:list')")
    public void download(HttpServletResponse response, BannerQueryCriteria criteria) throws IOException {
        BannerService.download(BannerService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询banner")
    @ApiOperation("查询banner")
    @PreAuthorize("@el.check('Banner:list')")
    public ResponseEntity<Object> query(BannerQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(BannerService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增banner")
    @ApiOperation("新增banner")
    @PreAuthorize("@el.check('Banner:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody Banner resources){
        return new ResponseEntity<>(BannerService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改banner")
    @ApiOperation("修改banner")
    @PreAuthorize("@el.check('Banner:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody Banner resources){
        BannerService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除banner")
    @ApiOperation("删除banner")
    @PreAuthorize("@el.check('Banner:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        BannerService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}