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
import me.zhengjie.api.domain.biz.VacancySkillTag;
import me.zhengjie.modules.biz.service.VacancySkillTagService;
import me.zhengjie.modules.biz.service.dto.VacancySkillTagQueryCriteria;
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
@Api(tags = "vacancy_skill_tag管理")
@RequestMapping("/api/VacancySkillTag")
public class VacancySkillTagController {

    private final VacancySkillTagService VacancySkillTagService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('VacancySkillTag:list')")
    public void download(HttpServletResponse response, VacancySkillTagQueryCriteria criteria) throws IOException {
        VacancySkillTagService.download(VacancySkillTagService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询vacancy_skill_tag")
    @ApiOperation("查询vacancy_skill_tag")
    @PreAuthorize("@el.check('VacancySkillTag:list')")
    public ResponseEntity<Object> query(VacancySkillTagQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(VacancySkillTagService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增vacancy_skill_tag")
    @ApiOperation("新增vacancy_skill_tag")
    @PreAuthorize("@el.check('VacancySkillTag:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody VacancySkillTag resources){
        return new ResponseEntity<>(VacancySkillTagService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改vacancy_skill_tag")
    @ApiOperation("修改vacancy_skill_tag")
    @PreAuthorize("@el.check('VacancySkillTag:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody VacancySkillTag resources){
        VacancySkillTagService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除vacancy_skill_tag")
    @ApiOperation("删除vacancy_skill_tag")
    @PreAuthorize("@el.check('VacancySkillTag:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        VacancySkillTagService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}