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

import me.zhengjie.api.domain.biz.VacancySkillTag;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.api.repository.biz.VacancySkillTagRepository;
import me.zhengjie.modules.biz.service.VacancySkillTagService;
import me.zhengjie.modules.biz.service.dto.VacancySkillTagDto;
import me.zhengjie.modules.biz.service.dto.VacancySkillTagQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.VacancySkillTagMapper;
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
public class VacancySkillTagServiceImpl implements VacancySkillTagService {

    private final VacancySkillTagRepository vacancySkillTagRepository;
    private final VacancySkillTagMapper vacancySkillTagMapper;

    @Override
    public Map<String,Object> queryAll(VacancySkillTagQueryCriteria criteria, Pageable pageable){
        Page<VacancySkillTag> page = vacancySkillTagRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(vacancySkillTagMapper::toDto));
    }

    @Override
    public List<VacancySkillTagDto> queryAll(VacancySkillTagQueryCriteria criteria){
        return vacancySkillTagMapper.toDto(vacancySkillTagRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public VacancySkillTagDto findById(Long id) {
        VacancySkillTag vacancySkillTag = vacancySkillTagRepository.findById(id).orElseGet(VacancySkillTag::new);
        ValidationUtil.isNull(vacancySkillTag.getId(),"VacancySkillTag","id",id);
        return vacancySkillTagMapper.toDto(vacancySkillTag);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VacancySkillTagDto create(VacancySkillTag resources) {
        return vacancySkillTagMapper.toDto(vacancySkillTagRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(VacancySkillTag resources) {
        VacancySkillTag vacancySkillTag = vacancySkillTagRepository.findById(resources.getId()).orElseGet(VacancySkillTag::new);
        ValidationUtil.isNull( vacancySkillTag.getId(),"VacancySkillTag","id",resources.getId());
        BeanUtil.copyProperties(resources, vacancySkillTag, CopyOptions.create().setIgnoreNullValue(true));
        vacancySkillTagRepository.save(vacancySkillTag);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            vacancySkillTagRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<VacancySkillTagDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (VacancySkillTagDto vacancySkillTag : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" vacancyId",  vacancySkillTag.getVacancyId());
            map.put(" skillTagId",  vacancySkillTag.getSkillTagId());
            map.put(" createdAt",  vacancySkillTag.getCreatedAt());
            map.put(" updatedAt",  vacancySkillTag.getUpdatedAt());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}