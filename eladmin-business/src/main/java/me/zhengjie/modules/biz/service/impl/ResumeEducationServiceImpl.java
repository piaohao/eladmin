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

import me.zhengjie.api.domain.biz.ResumeEducation;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.api.repository.biz.ResumeEducationRepository;
import me.zhengjie.modules.biz.service.ResumeEducationService;
import me.zhengjie.modules.biz.service.dto.ResumeEducationDto;
import me.zhengjie.modules.biz.service.dto.ResumeEducationQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.ResumeEducationMapper;
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
public class ResumeEducationServiceImpl implements ResumeEducationService {

    private final ResumeEducationRepository resumeEducationRepository;
    private final ResumeEducationMapper resumeEducationMapper;

    @Override
    public Map<String,Object> queryAll(ResumeEducationQueryCriteria criteria, Pageable pageable){
        Page<ResumeEducation> page = resumeEducationRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(resumeEducationMapper::toDto));
    }

    @Override
    public List<ResumeEducationDto> queryAll(ResumeEducationQueryCriteria criteria){
        return resumeEducationMapper.toDto(resumeEducationRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public ResumeEducationDto findById(Long id) {
        ResumeEducation resumeEducation = resumeEducationRepository.findById(id).orElseGet(ResumeEducation::new);
        ValidationUtil.isNull(resumeEducation.getId(),"ResumeEducation","id",id);
        return resumeEducationMapper.toDto(resumeEducation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResumeEducationDto create(ResumeEducation resources) {
        return resumeEducationMapper.toDto(resumeEducationRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ResumeEducation resources) {
        ResumeEducation resumeEducation = resumeEducationRepository.findById(resources.getId()).orElseGet(ResumeEducation::new);
        ValidationUtil.isNull( resumeEducation.getId(),"ResumeEducation","id",resources.getId());
        BeanUtil.copyProperties(resources, resumeEducation, CopyOptions.create().setIgnoreNullValue(true));
        resumeEducationRepository.save(resumeEducation);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            resumeEducationRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<ResumeEducationDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ResumeEducationDto resumeEducation : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" resumeId",  resumeEducation.getResumeId());
            map.put(" school",  resumeEducation.getSchool());
            map.put(" major",  resumeEducation.getMajor());
            map.put(" education",  resumeEducation.getEducation());
            map.put(" enrollmentTime",  resumeEducation.getEnrollmentTime());
            map.put(" graduationTime",  resumeEducation.getGraduationTime());
            map.put(" gpa",  resumeEducation.getGpa());
            map.put(" scoreRanking",  resumeEducation.getScoreRanking());
            map.put(" majorCourse",  resumeEducation.getMajorCourse());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}