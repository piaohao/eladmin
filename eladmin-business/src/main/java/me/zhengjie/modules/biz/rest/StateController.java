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
* @date 2020-06-04
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "State管理")
@RequestMapping("/api/state")
public class StateController {

    private final StateService stateService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('state:list')")
    public void download(HttpServletResponse response, StateQueryCriteria criteria) throws IOException {
        stateService.download(stateService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询State")
    @ApiOperation("查询State")
    @PreAuthorize("@el.check('state:list')")
    public ResponseEntity<Object> query(StateQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(stateService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增State")
    @ApiOperation("新增State")
    @PreAuthorize("@el.check('state:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody State resources){
        return new ResponseEntity<>(stateService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改State")
    @ApiOperation("修改State")
    @PreAuthorize("@el.check('state:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody State resources){
        stateService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除State")
    @ApiOperation("删除State")
    @PreAuthorize("@el.check('state:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        stateService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}