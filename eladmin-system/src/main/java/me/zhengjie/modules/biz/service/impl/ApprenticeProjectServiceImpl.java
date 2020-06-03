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

import me.zhengjie.api.domain.biz.ApprenticeProject;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.api.repository.biz.ApprenticeProjectRepository;
import me.zhengjie.modules.biz.service.ApprenticeProjectService;
import me.zhengjie.modules.biz.service.dto.ApprenticeProjectDto;
import me.zhengjie.modules.biz.service.dto.ApprenticeProjectQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.ApprenticeProjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.hutool.core.bean.BeanUtil;
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
* @date 2020-06-03
**/
@Service
@RequiredArgsConstructor
public class ApprenticeProjectServiceImpl implements ApprenticeProjectService {

    private final ApprenticeProjectRepository ApprenticeProjectRepository;
    private final ApprenticeProjectMapper ApprenticeProjectMapper;

    @Override
    public Map<String,Object> queryAll(ApprenticeProjectQueryCriteria criteria, Pageable pageable){
        Page<ApprenticeProject> page = ApprenticeProjectRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(ApprenticeProjectMapper::toDto));
    }

    @Override
    public List<ApprenticeProjectDto> queryAll(ApprenticeProjectQueryCriteria criteria){
        return ApprenticeProjectMapper.toDto(ApprenticeProjectRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public ApprenticeProjectDto findById(Long id) {
        ApprenticeProject ApprenticeProject = ApprenticeProjectRepository.findById(id).orElseGet(ApprenticeProject::new);
        ValidationUtil.isNull(ApprenticeProject.getId(),"ApprenticeProject","id",id);
        return ApprenticeProjectMapper.toDto(ApprenticeProject);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApprenticeProjectDto create(ApprenticeProject resources) {
        return ApprenticeProjectMapper.toDto(ApprenticeProjectRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ApprenticeProject resources) {
        ApprenticeProject ApprenticeProject = ApprenticeProjectRepository.findById(resources.getId()).orElseGet(ApprenticeProject::new);
        ValidationUtil.isNull( ApprenticeProject.getId(),"ApprenticeProject","id",resources.getId());
        BeanUtil.copyProperties(resources, ApprenticeProject, CopyOptions.create().setIgnoreNullValue(true));
        ApprenticeProjectRepository.save(ApprenticeProject);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            ApprenticeProjectRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<ApprenticeProjectDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ApprenticeProjectDto ApprenticeProject : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" schoolIds",  ApprenticeProject.getSchoolIds());
            map.put(" companyId",  ApprenticeProject.getCompanyId());
            map.put(" name",  ApprenticeProject.getName());
            map.put(" startAt",  ApprenticeProject.getStartAt());
            map.put(" endAt",  ApprenticeProject.getEndAt());
            map.put(" brief",  ApprenticeProject.getBrief());
            map.put(" departments",  ApprenticeProject.getDepartments());
            map.put(" headCount",  ApprenticeProject.getHeadCount());
            map.put("附件url", ApprenticeProject.getAttachmentUrl());
            map.put(" mentorIds",  ApprenticeProject.getMentorIds());
            map.put(" status",  ApprenticeProject.getStatus());
            map.put(" createdAt",  ApprenticeProject.getCreatedAt());
            map.put(" updatedAt",  ApprenticeProject.getUpdatedAt());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}