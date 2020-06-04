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

import me.zhengjie.api.domain.biz.ResumeProject;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.api.repository.biz.ResumeProjectRepository;
import me.zhengjie.modules.biz.service.ResumeProjectService;
import me.zhengjie.modules.biz.service.dto.ResumeProjectDto;
import me.zhengjie.modules.biz.service.dto.ResumeProjectQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.ResumeProjectMapper;
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
public class ResumeProjectServiceImpl implements ResumeProjectService {

    private final ResumeProjectRepository resumeProjectRepository;
    private final ResumeProjectMapper resumeProjectMapper;

    @Override
    public Map<String,Object> queryAll(ResumeProjectQueryCriteria criteria, Pageable pageable){
        Page<ResumeProject> page = resumeProjectRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(resumeProjectMapper::toDto));
    }

    @Override
    public List<ResumeProjectDto> queryAll(ResumeProjectQueryCriteria criteria){
        return resumeProjectMapper.toDto(resumeProjectRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public ResumeProjectDto findById(Long id) {
        ResumeProject resumeProject = resumeProjectRepository.findById(id).orElseGet(ResumeProject::new);
        ValidationUtil.isNull(resumeProject.getId(),"ResumeProject","id",id);
        return resumeProjectMapper.toDto(resumeProject);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResumeProjectDto create(ResumeProject resources) {
        return resumeProjectMapper.toDto(resumeProjectRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ResumeProject resources) {
        ResumeProject resumeProject = resumeProjectRepository.findById(resources.getId()).orElseGet(ResumeProject::new);
        ValidationUtil.isNull( resumeProject.getId(),"ResumeProject","id",resources.getId());
        BeanUtil.copyProperties(resources, resumeProject, CopyOptions.create().setIgnoreNullValue(true));
        resumeProjectRepository.save(resumeProject);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            resumeProjectRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<ResumeProjectDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ResumeProjectDto resumeProject : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" resumeId",  resumeProject.getResumeId());
            map.put(" project",  resumeProject.getProject());
            map.put(" startTime",  resumeProject.getStartTime());
            map.put(" endTime",  resumeProject.getEndTime());
            map.put(" duty",  resumeProject.getDuty());
            map.put(" description",  resumeProject.getDescription());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}