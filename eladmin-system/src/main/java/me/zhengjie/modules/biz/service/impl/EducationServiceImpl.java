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
public class EducationServiceImpl implements EducationService {

    private final EducationRepository EducationRepository;
    private final EducationMapper EducationMapper;

    @Override
    public Map<String,Object> queryAll(EducationQueryCriteria criteria, Pageable pageable){
        Page<Education> page = EducationRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(EducationMapper::toDto));
    }

    @Override
    public List<EducationDto> queryAll(EducationQueryCriteria criteria){
        return EducationMapper.toDto(EducationRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public EducationDto findById(Long id) {
        Education Education = EducationRepository.findById(id).orElseGet(Education::new);
        ValidationUtil.isNull(Education.getId(),"Education","id",id);
        return EducationMapper.toDto(Education);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public EducationDto create(Education resources) {
        return EducationMapper.toDto(EducationRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Education resources) {
        Education Education = EducationRepository.findById(resources.getId()).orElseGet(Education::new);
        ValidationUtil.isNull( Education.getId(),"Education","id",resources.getId());
        BeanUtil.copyProperties(resources, Education, CopyOptions.create().setIgnoreNullValue(true));
        EducationRepository.save(Education);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            EducationRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<EducationDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (EducationDto Education : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" name",  Education.getName());
            map.put(" createdAt",  Education.getCreatedAt());
            map.put(" updatedAt",  Education.getUpdatedAt());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}