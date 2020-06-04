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

import me.zhengjie.api.domain.biz.CompanyWelfare;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.api.repository.biz.CompanyWelfareRepository;
import me.zhengjie.modules.biz.service.CompanyWelfareService;
import me.zhengjie.modules.biz.service.dto.CompanyWelfareDto;
import me.zhengjie.modules.biz.service.dto.CompanyWelfareQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.CompanyWelfareMapper;
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
public class CompanyWelfareServiceImpl implements CompanyWelfareService {

    private final CompanyWelfareRepository companyWelfareRepository;
    private final CompanyWelfareMapper companyWelfareMapper;

    @Override
    public Map<String,Object> queryAll(CompanyWelfareQueryCriteria criteria, Pageable pageable){
        Page<CompanyWelfare> page = companyWelfareRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(companyWelfareMapper::toDto));
    }

    @Override
    public List<CompanyWelfareDto> queryAll(CompanyWelfareQueryCriteria criteria){
        return companyWelfareMapper.toDto(companyWelfareRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public CompanyWelfareDto findById(Long id) {
        CompanyWelfare companyWelfare = companyWelfareRepository.findById(id).orElseGet(CompanyWelfare::new);
        ValidationUtil.isNull(companyWelfare.getId(),"CompanyWelfare","id",id);
        return companyWelfareMapper.toDto(companyWelfare);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CompanyWelfareDto create(CompanyWelfare resources) {
        return companyWelfareMapper.toDto(companyWelfareRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(CompanyWelfare resources) {
        CompanyWelfare companyWelfare = companyWelfareRepository.findById(resources.getId()).orElseGet(CompanyWelfare::new);
        ValidationUtil.isNull( companyWelfare.getId(),"CompanyWelfare","id",resources.getId());
        BeanUtil.copyProperties(resources, companyWelfare, CopyOptions.create().setIgnoreNullValue(true));
        companyWelfareRepository.save(companyWelfare);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            companyWelfareRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<CompanyWelfareDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (CompanyWelfareDto companyWelfare : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" name",  companyWelfare.getName());
            map.put(" createdAt",  companyWelfare.getCreatedAt());
            map.put(" updatedAt",  companyWelfare.getUpdatedAt());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}