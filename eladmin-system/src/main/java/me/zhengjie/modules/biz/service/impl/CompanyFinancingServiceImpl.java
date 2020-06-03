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

import me.zhengjie.api.domain.biz.CompanyFinancing;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.api.repository.biz.CompanyFinancingRepository;
import me.zhengjie.modules.biz.service.CompanyFinancingService;
import me.zhengjie.modules.biz.service.dto.CompanyFinancingDto;
import me.zhengjie.modules.biz.service.dto.CompanyFinancingQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.CompanyFinancingMapper;
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
public class CompanyFinancingServiceImpl implements CompanyFinancingService {

    private final CompanyFinancingRepository CompanyFinancingRepository;
    private final CompanyFinancingMapper CompanyFinancingMapper;

    @Override
    public Map<String,Object> queryAll(CompanyFinancingQueryCriteria criteria, Pageable pageable){
        Page<CompanyFinancing> page = CompanyFinancingRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(CompanyFinancingMapper::toDto));
    }

    @Override
    public List<CompanyFinancingDto> queryAll(CompanyFinancingQueryCriteria criteria){
        return CompanyFinancingMapper.toDto(CompanyFinancingRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public CompanyFinancingDto findById(Long id) {
        CompanyFinancing CompanyFinancing = CompanyFinancingRepository.findById(id).orElseGet(CompanyFinancing::new);
        ValidationUtil.isNull(CompanyFinancing.getId(),"CompanyFinancing","id",id);
        return CompanyFinancingMapper.toDto(CompanyFinancing);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CompanyFinancingDto create(CompanyFinancing resources) {
        return CompanyFinancingMapper.toDto(CompanyFinancingRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(CompanyFinancing resources) {
        CompanyFinancing CompanyFinancing = CompanyFinancingRepository.findById(resources.getId()).orElseGet(CompanyFinancing::new);
        ValidationUtil.isNull( CompanyFinancing.getId(),"CompanyFinancing","id",resources.getId());
        BeanUtil.copyProperties(resources, CompanyFinancing, CopyOptions.create().setIgnoreNullValue(true));
        CompanyFinancingRepository.save(CompanyFinancing);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            CompanyFinancingRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<CompanyFinancingDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (CompanyFinancingDto CompanyFinancing : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" name",  CompanyFinancing.getName());
            map.put(" createdAt",  CompanyFinancing.getCreatedAt());
            map.put(" updatedAt",  CompanyFinancing.getUpdatedAt());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}