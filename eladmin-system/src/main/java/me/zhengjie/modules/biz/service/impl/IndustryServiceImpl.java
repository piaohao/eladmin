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

import me.zhengjie.api.domain.biz.Industry;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.api.repository.biz.IndustryRepository;
import me.zhengjie.modules.biz.service.IndustryService;
import me.zhengjie.modules.biz.service.dto.IndustryDto;
import me.zhengjie.modules.biz.service.dto.IndustryQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.IndustryMapper;
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
public class IndustryServiceImpl implements IndustryService {

    private final IndustryRepository IndustryRepository;
    private final IndustryMapper IndustryMapper;

    @Override
    public Map<String,Object> queryAll(IndustryQueryCriteria criteria, Pageable pageable){
        Page<Industry> page = IndustryRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(IndustryMapper::toDto));
    }

    @Override
    public List<IndustryDto> queryAll(IndustryQueryCriteria criteria){
        return IndustryMapper.toDto(IndustryRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public IndustryDto findById(Long id) {
        Industry Industry = IndustryRepository.findById(id).orElseGet(Industry::new);
        ValidationUtil.isNull(Industry.getId(),"Industry","id",id);
        return IndustryMapper.toDto(Industry);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public IndustryDto create(Industry resources) {
        return IndustryMapper.toDto(IndustryRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Industry resources) {
        Industry Industry = IndustryRepository.findById(resources.getId()).orElseGet(Industry::new);
        ValidationUtil.isNull( Industry.getId(),"Industry","id",resources.getId());
        BeanUtil.copyProperties(resources, Industry, CopyOptions.create().setIgnoreNullValue(true));
        IndustryRepository.save(Industry);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            IndustryRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<IndustryDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (IndustryDto Industry : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" name",  Industry.getName());
            map.put(" createdAt",  Industry.getCreatedAt());
            map.put(" updatedAt",  Industry.getUpdatedAt());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}