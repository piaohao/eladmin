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

import me.zhengjie.api.domain.biz.SalaryType;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.api.repository.biz.SalaryTypeRepository;
import me.zhengjie.modules.biz.service.SalaryTypeService;
import me.zhengjie.modules.biz.service.dto.SalaryTypeDto;
import me.zhengjie.modules.biz.service.dto.SalaryTypeQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.SalaryTypeMapper;
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
public class SalaryTypeServiceImpl implements SalaryTypeService {

    private final SalaryTypeRepository salaryTypeRepository;
    private final SalaryTypeMapper salaryTypeMapper;

    @Override
    public Map<String,Object> queryAll(SalaryTypeQueryCriteria criteria, Pageable pageable){
        Page<SalaryType> page = salaryTypeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(salaryTypeMapper::toDto));
    }

    @Override
    public List<SalaryTypeDto> queryAll(SalaryTypeQueryCriteria criteria){
        return salaryTypeMapper.toDto(salaryTypeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public SalaryTypeDto findById(Long id) {
        SalaryType salaryType = salaryTypeRepository.findById(id).orElseGet(SalaryType::new);
        ValidationUtil.isNull(salaryType.getId(),"SalaryType","id",id);
        return salaryTypeMapper.toDto(salaryType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SalaryTypeDto create(SalaryType resources) {
        return salaryTypeMapper.toDto(salaryTypeRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SalaryType resources) {
        SalaryType salaryType = salaryTypeRepository.findById(resources.getId()).orElseGet(SalaryType::new);
        ValidationUtil.isNull( salaryType.getId(),"SalaryType","id",resources.getId());
        BeanUtil.copyProperties(resources, salaryType, CopyOptions.create().setIgnoreNullValue(true));
        salaryTypeRepository.save(salaryType);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            salaryTypeRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<SalaryTypeDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SalaryTypeDto salaryType : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("日，月，年", salaryType.getType());
            map.put(" low",  salaryType.getLow());
            map.put(" high",  salaryType.getHigh());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}