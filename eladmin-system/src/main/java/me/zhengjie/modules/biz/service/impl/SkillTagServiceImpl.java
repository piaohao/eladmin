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
public class SkillTagServiceImpl implements SkillTagService {

    private final SkillTagRepository SkillTagRepository;
    private final SkillTagMapper SkillTagMapper;

    @Override
    public Map<String,Object> queryAll(SkillTagQueryCriteria criteria, Pageable pageable){
        Page<SkillTag> page = SkillTagRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(SkillTagMapper::toDto));
    }

    @Override
    public List<SkillTagDto> queryAll(SkillTagQueryCriteria criteria){
        return SkillTagMapper.toDto(SkillTagRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public SkillTagDto findById(Long id) {
        SkillTag SkillTag = SkillTagRepository.findById(id).orElseGet(SkillTag::new);
        ValidationUtil.isNull(SkillTag.getId(),"SkillTag","id",id);
        return SkillTagMapper.toDto(SkillTag);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SkillTagDto create(SkillTag resources) {
        return SkillTagMapper.toDto(SkillTagRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SkillTag resources) {
        SkillTag SkillTag = SkillTagRepository.findById(resources.getId()).orElseGet(SkillTag::new);
        ValidationUtil.isNull( SkillTag.getId(),"SkillTag","id",resources.getId());
        BeanUtil.copyProperties(resources, SkillTag, CopyOptions.create().setIgnoreNullValue(true));
        SkillTagRepository.save(SkillTag);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            SkillTagRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<SkillTagDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SkillTagDto SkillTag : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" name",  SkillTag.getName());
            map.put(" createdAt",  SkillTag.getCreatedAt());
            map.put(" updatedAt",  SkillTag.getUpdatedAt());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}