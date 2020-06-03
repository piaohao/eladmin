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
import me.zhengjie.api.domain.biz.VerifyCode;
import me.zhengjie.modules.biz.service.VerifyCodeService;
import me.zhengjie.modules.biz.service.dto.VerifyCodeQueryCriteria;
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
@Api(tags = "verify_code管理")
@RequestMapping("/api/VerifyCode")
public class VerifyCodeController {

    private final VerifyCodeService VerifyCodeService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('VerifyCode:list')")
    public void download(HttpServletResponse response, VerifyCodeQueryCriteria criteria) throws IOException {
        VerifyCodeService.download(VerifyCodeService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询verify_code")
    @ApiOperation("查询verify_code")
    @PreAuthorize("@el.check('VerifyCode:list')")
    public ResponseEntity<Object> query(VerifyCodeQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(VerifyCodeService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增verify_code")
    @ApiOperation("新增verify_code")
    @PreAuthorize("@el.check('VerifyCode:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody VerifyCode resources){
        return new ResponseEntity<>(VerifyCodeService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改verify_code")
    @ApiOperation("修改verify_code")
    @PreAuthorize("@el.check('VerifyCode:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody VerifyCode resources){
        VerifyCodeService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除verify_code")
    @ApiOperation("删除verify_code")
    @PreAuthorize("@el.check('VerifyCode:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        VerifyCodeService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}