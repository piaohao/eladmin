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
import me.zhengjie.api.domain.biz.ResumeSkill;
import me.zhengjie.modules.biz.service.ResumeSkillService;
import me.zhengjie.modules.biz.service.dto.ResumeSkillQueryCriteria;
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
@Api(tags = "ResumeSkill管理")
@RequestMapping("/api/resumeSkill")
public class ResumeSkillController {

    private final ResumeSkillService resumeSkillService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('resumeSkill:list')")
    public void download(HttpServletResponse response, ResumeSkillQueryCriteria criteria) throws IOException {
        resumeSkillService.download(resumeSkillService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询ResumeSkill")
    @ApiOperation("查询ResumeSkill")
    @PreAuthorize("@el.check('resumeSkill:list')")
    public ResponseEntity<Object> query(ResumeSkillQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(resumeSkillService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增ResumeSkill")
    @ApiOperation("新增ResumeSkill")
    @PreAuthorize("@el.check('resumeSkill:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody ResumeSkill resources){
        return new ResponseEntity<>(resumeSkillService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改ResumeSkill")
    @ApiOperation("修改ResumeSkill")
    @PreAuthorize("@el.check('resumeSkill:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody ResumeSkill resources){
        resumeSkillService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除ResumeSkill")
    @ApiOperation("删除ResumeSkill")
    @PreAuthorize("@el.check('resumeSkill:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        resumeSkillService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}