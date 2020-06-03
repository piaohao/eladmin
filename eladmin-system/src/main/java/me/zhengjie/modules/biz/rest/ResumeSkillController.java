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
* @date 2020-06-03
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "resume_skill管理")
@RequestMapping("/api/ResumeSkill")
public class ResumeSkillController {

    private final ResumeSkillService ResumeSkillService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('ResumeSkill:list')")
    public void download(HttpServletResponse response, ResumeSkillQueryCriteria criteria) throws IOException {
        ResumeSkillService.download(ResumeSkillService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询resume_skill")
    @ApiOperation("查询resume_skill")
    @PreAuthorize("@el.check('ResumeSkill:list')")
    public ResponseEntity<Object> query(ResumeSkillQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(ResumeSkillService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增resume_skill")
    @ApiOperation("新增resume_skill")
    @PreAuthorize("@el.check('ResumeSkill:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody ResumeSkill resources){
        return new ResponseEntity<>(ResumeSkillService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改resume_skill")
    @ApiOperation("修改resume_skill")
    @PreAuthorize("@el.check('ResumeSkill:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody ResumeSkill resources){
        ResumeSkillService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除resume_skill")
    @ApiOperation("删除resume_skill")
    @PreAuthorize("@el.check('ResumeSkill:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        ResumeSkillService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}