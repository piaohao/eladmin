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
public class CompanyFinancingServiceImpl implements CompanyFinancingService {

    private final CompanyFinancingRepository companyFinancingRepository;
    private final CompanyFinancingMapper companyFinancingMapper;

    @Override
    public Map<String,Object> queryAll(CompanyFinancingQueryCriteria criteria, Pageable pageable){
        Page<CompanyFinancing> page = companyFinancingRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(companyFinancingMapper::toDto));
    }

    @Override
    public List<CompanyFinancingDto> queryAll(CompanyFinancingQueryCriteria criteria){
        return companyFinancingMapper.toDto(companyFinancingRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public CompanyFinancingDto findById(Long id) {
        CompanyFinancing companyFinancing = companyFinancingRepository.findById(id).orElseGet(CompanyFinancing::new);
        ValidationUtil.isNull(companyFinancing.getId(),"CompanyFinancing","id",id);
        return companyFinancingMapper.toDto(companyFinancing);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CompanyFinancingDto create(CompanyFinancing resources) {
        return companyFinancingMapper.toDto(companyFinancingRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(CompanyFinancing resources) {
        CompanyFinancing companyFinancing = companyFinancingRepository.findById(resources.getId()).orElseGet(CompanyFinancing::new);
        ValidationUtil.isNull( companyFinancing.getId(),"CompanyFinancing","id",resources.getId());
        BeanUtil.copyProperties(resources, companyFinancing, CopyOptions.create().setIgnoreNullValue(true));
        companyFinancingRepository.save(companyFinancing);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            companyFinancingRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<CompanyFinancingDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (CompanyFinancingDto companyFinancing : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" name",  companyFinancing.getName());
            map.put(" createdAt",  companyFinancing.getCreatedAt());
            map.put(" updatedAt",  companyFinancing.getUpdatedAt());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}