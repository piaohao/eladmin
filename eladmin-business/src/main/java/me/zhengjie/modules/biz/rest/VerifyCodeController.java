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
import me.zhengjie.api.domain.biz.VerifyCode;
import me.zhengjie.modules.biz.service.VerifyCodeService;
import me.zhengjie.modules.biz.service.dto.VerifyCodeQueryCriteria;
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
@Api(tags = "VerifyCode管理")
@RequestMapping("/api/verifyCode")
public class VerifyCodeController {

    private final VerifyCodeService verifyCodeService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('verifyCode:list')")
    public void download(HttpServletResponse response, VerifyCodeQueryCriteria criteria) throws IOException {
        verifyCodeService.download(verifyCodeService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询VerifyCode")
    @ApiOperation("查询VerifyCode")
    @PreAuthorize("@el.check('verifyCode:list')")
    public ResponseEntity<Object> query(VerifyCodeQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(verifyCodeService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增VerifyCode")
    @ApiOperation("新增VerifyCode")
    @PreAuthorize("@el.check('verifyCode:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody VerifyCode resources){
        return new ResponseEntity<>(verifyCodeService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改VerifyCode")
    @ApiOperation("修改VerifyCode")
    @PreAuthorize("@el.check('verifyCode:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody VerifyCode resources){
        verifyCodeService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除VerifyCode")
    @ApiOperation("删除VerifyCode")
    @PreAuthorize("@el.check('verifyCode:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        verifyCodeService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}