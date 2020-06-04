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

import me.zhengjie.api.domain.biz.Education;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.api.repository.biz.EducationRepository;
import me.zhengjie.modules.biz.service.EducationService;
import me.zhengjie.modules.biz.service.dto.EducationDto;
import me.zhengjie.modules.biz.service.dto.EducationQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.EducationMapper;
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
public class EducationServiceImpl implements EducationService {

    private final EducationRepository educationRepository;
    private final EducationMapper educationMapper;

    @Override
    public Map<String,Object> queryAll(EducationQueryCriteria criteria, Pageable pageable){
        Page<Education> page = educationRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(educationMapper::toDto));
    }

    @Override
    public List<EducationDto> queryAll(EducationQueryCriteria criteria){
        return educationMapper.toDto(educationRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public EducationDto findById(Long id) {
        Education education = educationRepository.findById(id).orElseGet(Education::new);
        ValidationUtil.isNull(education.getId(),"Education","id",id);
        return educationMapper.toDto(education);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public EducationDto create(Education resources) {
        return educationMapper.toDto(educationRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Education resources) {
        Education education = educationRepository.findById(resources.getId()).orElseGet(Education::new);
        ValidationUtil.isNull( education.getId(),"Education","id",resources.getId());
        BeanUtil.copyProperties(resources, education, CopyOptions.create().setIgnoreNullValue(true));
        educationRepository.save(education);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            educationRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<EducationDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (EducationDto education : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" name",  education.getName());
            map.put(" createdAt",  education.getCreatedAt());
            map.put(" updatedAt",  education.getUpdatedAt());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}