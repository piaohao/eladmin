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
* @date 2020-06-04
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "VacancySkillTag管理")
@RequestMapping("/api/vacancySkillTag")
public class VacancySkillTagController {

    private final VacancySkillTagService vacancySkillTagService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('vacancySkillTag:list')")
    public void download(HttpServletResponse response, VacancySkillTagQueryCriteria criteria) throws IOException {
        vacancySkillTagService.download(vacancySkillTagService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询VacancySkillTag")
    @ApiOperation("查询VacancySkillTag")
    @PreAuthorize("@el.check('vacancySkillTag:list')")
    public ResponseEntity<Object> query(VacancySkillTagQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(vacancySkillTagService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增VacancySkillTag")
    @ApiOperation("新增VacancySkillTag")
    @PreAuthorize("@el.check('vacancySkillTag:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody VacancySkillTag resources){
        return new ResponseEntity<>(vacancySkillTagService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改VacancySkillTag")
    @ApiOperation("修改VacancySkillTag")
    @PreAuthorize("@el.check('vacancySkillTag:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody VacancySkillTag resources){
        vacancySkillTagService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除VacancySkillTag")
    @ApiOperation("删除VacancySkillTag")
    @PreAuthorize("@el.check('vacancySkillTag:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        vacancySkillTagService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}