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

import me.zhengjie.api.domain.biz.CompanyScale;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.api.repository.biz.CompanyScaleRepository;
import me.zhengjie.modules.biz.service.CompanyScaleService;
import me.zhengjie.modules.biz.service.dto.CompanyScaleDto;
import me.zhengjie.modules.biz.service.dto.CompanyScaleQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.CompanyScaleMapper;
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
public class CompanyScaleServiceImpl implements CompanyScaleService {

    private final CompanyScaleRepository companyScaleRepository;
    private final CompanyScaleMapper companyScaleMapper;

    @Override
    public Map<String,Object> queryAll(CompanyScaleQueryCriteria criteria, Pageable pageable){
        Page<CompanyScale> page = companyScaleRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(companyScaleMapper::toDto));
    }

    @Override
    public List<CompanyScaleDto> queryAll(CompanyScaleQueryCriteria criteria){
        return companyScaleMapper.toDto(companyScaleRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public CompanyScaleDto findById(Long id) {
        CompanyScale companyScale = companyScaleRepository.findById(id).orElseGet(CompanyScale::new);
        ValidationUtil.isNull(companyScale.getId(),"CompanyScale","id",id);
        return companyScaleMapper.toDto(companyScale);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CompanyScaleDto create(CompanyScale resources) {
        return companyScaleMapper.toDto(companyScaleRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(CompanyScale resources) {
        CompanyScale companyScale = companyScaleRepository.findById(resources.getId()).orElseGet(CompanyScale::new);
        ValidationUtil.isNull( companyScale.getId(),"CompanyScale","id",resources.getId());
        BeanUtil.copyProperties(resources, companyScale, CopyOptions.create().setIgnoreNullValue(true));
        companyScaleRepository.save(companyScale);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            companyScaleRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<CompanyScaleDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (CompanyScaleDto companyScale : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" name",  companyScale.getName());
            map.put(" createdAt",  companyScale.getCreatedAt());
            map.put(" updatedAt",  companyScale.getUpdatedAt());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}