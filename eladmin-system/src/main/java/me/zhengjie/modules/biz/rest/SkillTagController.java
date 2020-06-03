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
import me.zhengjie.api.domain.biz.SkillTag;
import me.zhengjie.modules.biz.service.SkillTagService;
import me.zhengjie.modules.biz.service.dto.SkillTagQueryCriteria;
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
@Api(tags = "skill_tag管理")
@RequestMapping("/api/SkillTag")
public class SkillTagController {

    private final SkillTagService SkillTagService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('SkillTag:list')")
    public void download(HttpServletResponse response, SkillTagQueryCriteria criteria) throws IOException {
        SkillTagService.download(SkillTagService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询skill_tag")
    @ApiOperation("查询skill_tag")
    @PreAuthorize("@el.check('SkillTag:list')")
    public ResponseEntity<Object> query(SkillTagQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(SkillTagService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增skill_tag")
    @ApiOperation("新增skill_tag")
    @PreAuthorize("@el.check('SkillTag:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody SkillTag resources){
        return new ResponseEntity<>(SkillTagService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改skill_tag")
    @ApiOperation("修改skill_tag")
    @PreAuthorize("@el.check('SkillTag:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody SkillTag resources){
        SkillTagService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除skill_tag")
    @ApiOperation("删除skill_tag")
    @PreAuthorize("@el.check('SkillTag:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        SkillTagService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}