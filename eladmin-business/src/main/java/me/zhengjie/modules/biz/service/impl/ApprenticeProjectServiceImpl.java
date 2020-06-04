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
public class ApprenticeProjectServiceImpl implements ApprenticeProjectService {

    private final ApprenticeProjectRepository apprenticeProjectRepository;
    private final ApprenticeProjectMapper apprenticeProjectMapper;

    @Override
    public Map<String,Object> queryAll(ApprenticeProjectQueryCriteria criteria, Pageable pageable){
        Page<ApprenticeProject> page = apprenticeProjectRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(apprenticeProjectMapper::toDto));
    }

    @Override
    public List<ApprenticeProjectDto> queryAll(ApprenticeProjectQueryCriteria criteria){
        return apprenticeProjectMapper.toDto(apprenticeProjectRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public ApprenticeProjectDto findById(Long id) {
        ApprenticeProject apprenticeProject = apprenticeProjectRepository.findById(id).orElseGet(ApprenticeProject::new);
        ValidationUtil.isNull(apprenticeProject.getId(),"ApprenticeProject","id",id);
        return apprenticeProjectMapper.toDto(apprenticeProject);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApprenticeProjectDto create(ApprenticeProject resources) {
        return apprenticeProjectMapper.toDto(apprenticeProjectRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ApprenticeProject resources) {
        ApprenticeProject apprenticeProject = apprenticeProjectRepository.findById(resources.getId()).orElseGet(ApprenticeProject::new);
        ValidationUtil.isNull( apprenticeProject.getId(),"ApprenticeProject","id",resources.getId());
        BeanUtil.copyProperties(resources, apprenticeProject, CopyOptions.create().setIgnoreNullValue(true));
        apprenticeProjectRepository.save(apprenticeProject);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            apprenticeProjectRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<ApprenticeProjectDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ApprenticeProjectDto apprenticeProject : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" schoolIds",  apprenticeProject.getSchoolIds());
            map.put(" companyId",  apprenticeProject.getCompanyId());
            map.put(" name",  apprenticeProject.getName());
            map.put(" startAt",  apprenticeProject.getStartAt());
            map.put(" endAt",  apprenticeProject.getEndAt());
            map.put(" brief",  apprenticeProject.getBrief());
            map.put(" departments",  apprenticeProject.getDepartments());
            map.put(" headCount",  apprenticeProject.getHeadCount());
            map.put("附件url", apprenticeProject.getAttachmentUrl());
            map.put(" mentorIds",  apprenticeProject.getMentorIds());
            map.put(" status",  apprenticeProject.getStatus());
            map.put(" createdAt",  apprenticeProject.getCreatedAt());
            map.put(" updatedAt",  apprenticeProject.getUpdatedAt());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}