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

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.RequiredArgsConstructor;
import me.zhengjie.api.domain.biz.ResumeProject;
import me.zhengjie.api.repository.biz.ResumeProjectRepository;
import me.zhengjie.modules.biz.service.ResumeProjectService;
import me.zhengjie.modules.biz.service.dto.ResumeProjectDto;
import me.zhengjie.modules.biz.service.dto.ResumeProjectQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.ResumeProjectMapper;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import me.zhengjie.utils.ValidationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
* @website https://el-admin.vip
* @description 服务实现
* @author piaohao
* @date 2020-06-03
**/
@Service
@RequiredArgsConstructor
public class ResumeProjectServiceImpl implements ResumeProjectService {

    private final ResumeProjectRepository ResumeProjectRepository;
    private final ResumeProjectMapper ResumeProjectMapper;

    @Override
    public Map<String,Object> queryAll(ResumeProjectQueryCriteria criteria, Pageable pageable){
        Page<ResumeProject> page = ResumeProjectRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(ResumeProjectMapper::toDto));
    }

    @Override
    public List<ResumeProjectDto> queryAll(ResumeProjectQueryCriteria criteria){
        return ResumeProjectMapper.toDto(ResumeProjectRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public ResumeProjectDto findById(Long id) {
        ResumeProject ResumeProject = ResumeProjectRepository.findById(id).orElseGet(ResumeProject::new);
        ValidationUtil.isNull(ResumeProject.getId(),"ResumeProject","id",id);
        return ResumeProjectMapper.toDto(ResumeProject);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResumeProjectDto create(ResumeProject resources) {
        return ResumeProjectMapper.toDto(ResumeProjectRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ResumeProject resources) {
        ResumeProject ResumeProject = ResumeProjectRepository.findById(resources.getId()).orElseGet(ResumeProject::new);
        ValidationUtil.isNull( ResumeProject.getId(),"ResumeProject","id",resources.getId());
        BeanUtil.copyProperties(resources, ResumeProject, CopyOptions.create().setIgnoreNullValue(true));
        ResumeProjectRepository.save(ResumeProject);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            ResumeProjectRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<ResumeProjectDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ResumeProjectDto ResumeProject : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" resumeId",  ResumeProject.getResumeId());
            map.put(" project",  ResumeProject.getProject());
            map.put(" startTime",  ResumeProject.getStartTime());
            map.put(" endTime",  ResumeProject.getEndTime());
            map.put(" duty",  ResumeProject.getDuty());
            map.put(" description",  ResumeProject.getDescription());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}