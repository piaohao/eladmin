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
import me.zhengjie.api.domain.biz.UnionProject;
import me.zhengjie.api.repository.biz.UnionProjectRepository;
import me.zhengjie.modules.biz.service.UnionProjectService;
import me.zhengjie.modules.biz.service.dto.UnionProjectDto;
import me.zhengjie.modules.biz.service.dto.UnionProjectQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.UnionProjectMapper;
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
public class UnionProjectServiceImpl implements UnionProjectService {

    private final UnionProjectRepository UnionProjectRepository;
    private final UnionProjectMapper UnionProjectMapper;

    @Override
    public Map<String,Object> queryAll(UnionProjectQueryCriteria criteria, Pageable pageable){
        Page<UnionProject> page = UnionProjectRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(UnionProjectMapper::toDto));
    }

    @Override
    public List<UnionProjectDto> queryAll(UnionProjectQueryCriteria criteria){
        return UnionProjectMapper.toDto(UnionProjectRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public UnionProjectDto findById(Long id) {
        UnionProject UnionProject = UnionProjectRepository.findById(id).orElseGet(UnionProject::new);
        ValidationUtil.isNull(UnionProject.getId(),"UnionProject","id",id);
        return UnionProjectMapper.toDto(UnionProject);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UnionProjectDto create(UnionProject resources) {
        return UnionProjectMapper.toDto(UnionProjectRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(UnionProject resources) {
        UnionProject UnionProject = UnionProjectRepository.findById(resources.getId()).orElseGet(UnionProject::new);
        ValidationUtil.isNull( UnionProject.getId(),"UnionProject","id",resources.getId());
        BeanUtil.copyProperties(resources, UnionProject, CopyOptions.create().setIgnoreNullValue(true));
        UnionProjectRepository.save(UnionProject);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            UnionProjectRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<UnionProjectDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (UnionProjectDto UnionProject : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" schoolId",  UnionProject.getSchoolId());
            map.put("学院id", UnionProject.getCollegeId());
            map.put(" companyId",  UnionProject.getCompanyId());
            map.put("职位id", UnionProject.getVacancyId());
            map.put(" coType",  UnionProject.getCoType());
            map.put(" coDuration",  UnionProject.getCoDuration());
            map.put(" status",  UnionProject.getStatus());
            map.put(" createdAt",  UnionProject.getCreatedAt());
            map.put(" updatedAt",  UnionProject.getUpdatedAt());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}