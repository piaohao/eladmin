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
import me.zhengjie.api.domain.biz.State;
import me.zhengjie.modules.biz.service.StateService;
import me.zhengjie.modules.biz.service.dto.StateQueryCriteria;
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
@Api(tags = "state管理")
@RequestMapping("/api/State")
public class StateController {

    private final StateService StateService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('State:list')")
    public void download(HttpServletResponse response, StateQueryCriteria criteria) throws IOException {
        StateService.download(StateService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询state")
    @ApiOperation("查询state")
    @PreAuthorize("@el.check('State:list')")
    public ResponseEntity<Object> query(StateQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(StateService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增state")
    @ApiOperation("新增state")
    @PreAuthorize("@el.check('State:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody State resources){
        return new ResponseEntity<>(StateService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改state")
    @ApiOperation("修改state")
    @PreAuthorize("@el.check('State:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody State resources){
        StateService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除state")
    @ApiOperation("删除state")
    @PreAuthorize("@el.check('State:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        StateService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}