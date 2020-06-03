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
import me.zhengjie.api.domain.biz.Message;
import me.zhengjie.modules.biz.service.MessageService;
import me.zhengjie.modules.biz.service.dto.MessageQueryCriteria;
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
@Api(tags = "message管理")
@RequestMapping("/api/Message")
public class MessageController {

    private final MessageService MessageService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('Message:list')")
    public void download(HttpServletResponse response, MessageQueryCriteria criteria) throws IOException {
        MessageService.download(MessageService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询message")
    @ApiOperation("查询message")
    @PreAuthorize("@el.check('Message:list')")
    public ResponseEntity<Object> query(MessageQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(MessageService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增message")
    @ApiOperation("新增message")
    @PreAuthorize("@el.check('Message:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody Message resources){
        return new ResponseEntity<>(MessageService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改message")
    @ApiOperation("修改message")
    @PreAuthorize("@el.check('Message:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody Message resources){
        MessageService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除message")
    @ApiOperation("删除message")
    @PreAuthorize("@el.check('Message:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        MessageService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}