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

import me.zhengjie.api.domain.biz.SkillTag;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.api.repository.biz.SkillTagRepository;
import me.zhengjie.modules.biz.service.SkillTagService;
import me.zhengjie.modules.biz.service.dto.SkillTagDto;
import me.zhengjie.modules.biz.service.dto.SkillTagQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.SkillTagMapper;
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
public class SkillTagServiceImpl implements SkillTagService {

    private final SkillTagRepository skillTagRepository;
    private final SkillTagMapper skillTagMapper;

    @Override
    public Map<String,Object> queryAll(SkillTagQueryCriteria criteria, Pageable pageable){
        Page<SkillTag> page = skillTagRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(skillTagMapper::toDto));
    }

    @Override
    public List<SkillTagDto> queryAll(SkillTagQueryCriteria criteria){
        return skillTagMapper.toDto(skillTagRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public SkillTagDto findById(Long id) {
        SkillTag skillTag = skillTagRepository.findById(id).orElseGet(SkillTag::new);
        ValidationUtil.isNull(skillTag.getId(),"SkillTag","id",id);
        return skillTagMapper.toDto(skillTag);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SkillTagDto create(SkillTag resources) {
        return skillTagMapper.toDto(skillTagRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SkillTag resources) {
        SkillTag skillTag = skillTagRepository.findById(resources.getId()).orElseGet(SkillTag::new);
        ValidationUtil.isNull( skillTag.getId(),"SkillTag","id",resources.getId());
        BeanUtil.copyProperties(resources, skillTag, CopyOptions.create().setIgnoreNullValue(true));
        skillTagRepository.save(skillTag);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            skillTagRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<SkillTagDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SkillTagDto skillTag : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" name",  skillTag.getName());
            map.put(" createdAt",  skillTag.getCreatedAt());
            map.put(" updatedAt",  skillTag.getUpdatedAt());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}