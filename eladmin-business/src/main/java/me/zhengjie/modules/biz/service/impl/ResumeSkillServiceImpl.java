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
package me.zhengjie.modules.biz.service.impl;

import me.zhengjie.api.domain.biz.ResumeSkill;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.api.repository.biz.ResumeSkillRepository;
import me.zhengjie.modules.biz.service.ResumeSkillService;
import me.zhengjie.modules.biz.service.dto.ResumeSkillDto;
import me.zhengjie.modules.biz.service.dto.ResumeSkillQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.ResumeSkillMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* @website https://el-admin.vip
* @description 服务实现
* @author piaohao
* @date 2020-06-04
**/
@Service
@RequiredArgsConstructor
public class ResumeSkillServiceImpl implements ResumeSkillService {

    private final ResumeSkillRepository resumeSkillRepository;
    private final ResumeSkillMapper resumeSkillMapper;

    @Override
    public Map<String,Object> queryAll(ResumeSkillQueryCriteria criteria, Pageable pageable){
        Page<ResumeSkill> page = resumeSkillRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(resumeSkillMapper::toDto));
    }

    @Override
    public List<ResumeSkillDto> queryAll(ResumeSkillQueryCriteria criteria){
        return resumeSkillMapper.toDto(resumeSkillRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public ResumeSkillDto findById(Long id) {
        ResumeSkill resumeSkill = resumeSkillRepository.findById(id).orElseGet(ResumeSkill::new);
        ValidationUtil.isNull(resumeSkill.getId(),"ResumeSkill","id",id);
        return resumeSkillMapper.toDto(resumeSkill);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResumeSkillDto create(ResumeSkill resources) {
        return resumeSkillMapper.toDto(resumeSkillRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ResumeSkill resources) {
        ResumeSkill resumeSkill = resumeSkillRepository.findById(resources.getId()).orElseGet(ResumeSkill::new);
        ValidationUtil.isNull( resumeSkill.getId(),"ResumeSkill","id",resources.getId());
        BeanUtil.copyProperties(resources, resumeSkill, CopyOptions.create().setIgnoreNullValue(true));
        resumeSkillRepository.save(resumeSkill);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            resumeSkillRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<ResumeSkillDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ResumeSkillDto resumeSkill : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" resumeId",  resumeSkill.getResumeId());
            map.put(" skillName",  resumeSkill.getSkillName());
            map.put(" level",  resumeSkill.getLevel());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}