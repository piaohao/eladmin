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

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.RequiredArgsConstructor;
import me.zhengjie.api.domain.biz.Company;
import me.zhengjie.api.repository.biz.CompanyRepository;
import me.zhengjie.modules.biz.service.CompanyService;
import me.zhengjie.modules.biz.service.dto.CompanyDto;
import me.zhengjie.modules.biz.service.dto.CompanyQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.CompanyMapper;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import me.zhengjie.utils.ValidationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
* @website https://el-admin.vip
* @description 服务实现
* @author piaohao
* @date 2020-06-03
**/
@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository CompanyRepository;
    private final CompanyMapper CompanyMapper;

    @Override
    public Map<String,Object> queryAll(CompanyQueryCriteria criteria, Pageable pageable){
        Page<Company> page = CompanyRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(CompanyMapper::toDto));
    }

    @Override
    public List<CompanyDto> queryAll(CompanyQueryCriteria criteria){
        return CompanyMapper.toDto(CompanyRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public CompanyDto findById(Long id) {
        Company Company = CompanyRepository.findById(id).orElseGet(Company::new);
        ValidationUtil.isNull(Company.getId(),"Company","id",id);
        return CompanyMapper.toDto(Company);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CompanyDto create(Company resources) {
        return CompanyMapper.toDto(CompanyRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Company resources) {
        Company Company = CompanyRepository.findById(resources.getId()).orElseGet(Company::new);
        ValidationUtil.isNull( Company.getId(),"Company","id",resources.getId());
        BeanUtil.copyProperties(resources, Company, CopyOptions.create().setIgnoreNullValue(true));
        CompanyRepository.save(Company);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            CompanyRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<CompanyDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (CompanyDto Company : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" name",  Company.getName());
            map.put(" logo",  Company.getLogo());
            map.put(" address",  Company.getAddress());
            map.put(" homePage",  Company.getHomePage());
            map.put(" profile",  Company.getProfile());
            map.put(" tags",  Company.getTags());
            map.put(" photos",  Company.getPhotos());
            map.put(" lat",  Company.getLat());
            map.put(" lng",  Company.getLng());
            map.put(" createdAt",  Company.getCreatedAt());
            map.put(" updatedAt",  Company.getUpdatedAt());
            map.put(" isStar",  Company.getIsStar());
            map.put(" companyScaleId",  Company.getCompanyScaleId());
            map.put(" companyFinancingId",  Company.getCompanyFinancingId());
            map.put(" stateId",  Company.getStateId());
            map.put(" cityId",  Company.getCityId());
            map.put(" districtId",  Company.getDistrictId());
            map.put(" industryId",  Company.getIndustryId());
            map.put(" welfareId",  Company.getWelfareId());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}