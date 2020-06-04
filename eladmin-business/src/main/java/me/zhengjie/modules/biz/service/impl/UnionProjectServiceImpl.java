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

import me.zhengjie.api.domain.biz.UnionProject;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.api.repository.biz.UnionProjectRepository;
import me.zhengjie.modules.biz.service.UnionProjectService;
import me.zhengjie.modules.biz.service.dto.UnionProjectDto;
import me.zhengjie.modules.biz.service.dto.UnionProjectQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.UnionProjectMapper;
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
public class UnionProjectServiceImpl implements UnionProjectService {

    private final UnionProjectRepository unionProjectRepository;
    private final UnionProjectMapper unionProjectMapper;

    @Override
    public Map<String,Object> queryAll(UnionProjectQueryCriteria criteria, Pageable pageable){
        Page<UnionProject> page = unionProjectRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(unionProjectMapper::toDto));
    }

    @Override
    public List<UnionProjectDto> queryAll(UnionProjectQueryCriteria criteria){
        return unionProjectMapper.toDto(unionProjectRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public UnionProjectDto findById(Long id) {
        UnionProject unionProject = unionProjectRepository.findById(id).orElseGet(UnionProject::new);
        ValidationUtil.isNull(unionProject.getId(),"UnionProject","id",id);
        return unionProjectMapper.toDto(unionProject);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UnionProjectDto create(UnionProject resources) {
        return unionProjectMapper.toDto(unionProjectRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(UnionProject resources) {
        UnionProject unionProject = unionProjectRepository.findById(resources.getId()).orElseGet(UnionProject::new);
        ValidationUtil.isNull( unionProject.getId(),"UnionProject","id",resources.getId());
        BeanUtil.copyProperties(resources, unionProject, CopyOptions.create().setIgnoreNullValue(true));
        unionProjectRepository.save(unionProject);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            unionProjectRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<UnionProjectDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (UnionProjectDto unionProject : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" schoolId",  unionProject.getSchoolId());
            map.put("学院id", unionProject.getCollegeId());
            map.put(" companyId",  unionProject.getCompanyId());
            map.put("职位id", unionProject.getVacancyId());
            map.put(" coType",  unionProject.getCoType());
            map.put(" coDuration",  unionProject.getCoDuration());
            map.put(" status",  unionProject.getStatus());
            map.put(" createdAt",  unionProject.getCreatedAt());
            map.put(" updatedAt",  unionProject.getUpdatedAt());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}