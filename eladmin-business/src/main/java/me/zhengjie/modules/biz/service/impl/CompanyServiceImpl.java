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

import me.zhengjie.api.domain.biz.Company;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.api.repository.biz.CompanyRepository;
import me.zhengjie.modules.biz.service.CompanyService;
import me.zhengjie.modules.biz.service.dto.CompanyDto;
import me.zhengjie.modules.biz.service.dto.CompanyQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.CompanyMapper;
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
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;

    @Override
    public Map<String,Object> queryAll(CompanyQueryCriteria criteria, Pageable pageable){
        Page<Company> page = companyRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(companyMapper::toDto));
    }

    @Override
    public List<CompanyDto> queryAll(CompanyQueryCriteria criteria){
        return companyMapper.toDto(companyRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public CompanyDto findById(Long id) {
        Company company = companyRepository.findById(id).orElseGet(Company::new);
        ValidationUtil.isNull(company.getId(),"Company","id",id);
        return companyMapper.toDto(company);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CompanyDto create(Company resources) {
        return companyMapper.toDto(companyRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Company resources) {
        Company company = companyRepository.findById(resources.getId()).orElseGet(Company::new);
        ValidationUtil.isNull( company.getId(),"Company","id",resources.getId());
        BeanUtil.copyProperties(resources, company, CopyOptions.create().setIgnoreNullValue(true));
        companyRepository.save(company);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            companyRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<CompanyDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (CompanyDto company : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" name",  company.getName());
            map.put(" logo",  company.getLogo());
            map.put(" address",  company.getAddress());
            map.put(" homePage",  company.getHomePage());
            map.put(" profile",  company.getProfile());
            map.put(" tags",  company.getTags());
            map.put(" photos",  company.getPhotos());
            map.put(" lat",  company.getLat());
            map.put(" lng",  company.getLng());
            map.put(" createdAt",  company.getCreatedAt());
            map.put(" updatedAt",  company.getUpdatedAt());
            map.put(" isStar",  company.getIsStar());
            map.put(" companyScaleId",  company.getCompanyScaleId());
            map.put(" companyFinancingId",  company.getCompanyFinancingId());
            map.put(" stateId",  company.getStateId());
            map.put(" cityId",  company.getCityId());
            map.put(" districtId",  company.getDistrictId());
            map.put(" industryId",  company.getIndustryId());
            map.put(" welfareId",  company.getWelfareId());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}