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
import me.zhengjie.api.domain.biz.SalaryType;
import me.zhengjie.api.repository.biz.SalaryTypeRepository;
import me.zhengjie.modules.biz.service.SalaryTypeService;
import me.zhengjie.modules.biz.service.dto.SalaryTypeDto;
import me.zhengjie.modules.biz.service.dto.SalaryTypeQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.SalaryTypeMapper;
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
public class SalaryTypeServiceImpl implements SalaryTypeService {

    private final SalaryTypeRepository SalaryTypeRepository;
    private final SalaryTypeMapper SalaryTypeMapper;

    @Override
    public Map<String,Object> queryAll(SalaryTypeQueryCriteria criteria, Pageable pageable){
        Page<SalaryType> page = SalaryTypeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(SalaryTypeMapper::toDto));
    }

    @Override
    public List<SalaryTypeDto> queryAll(SalaryTypeQueryCriteria criteria){
        return SalaryTypeMapper.toDto(SalaryTypeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public SalaryTypeDto findById(Long id) {
        SalaryType SalaryType = SalaryTypeRepository.findById(id).orElseGet(SalaryType::new);
        ValidationUtil.isNull(SalaryType.getId(),"SalaryType","id",id);
        return SalaryTypeMapper.toDto(SalaryType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SalaryTypeDto create(SalaryType resources) {
        return SalaryTypeMapper.toDto(SalaryTypeRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SalaryType resources) {
        SalaryType SalaryType = SalaryTypeRepository.findById(resources.getId()).orElseGet(SalaryType::new);
        ValidationUtil.isNull( SalaryType.getId(),"SalaryType","id",resources.getId());
        BeanUtil.copyProperties(resources, SalaryType, CopyOptions.create().setIgnoreNullValue(true));
        SalaryTypeRepository.save(SalaryType);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            SalaryTypeRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<SalaryTypeDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SalaryTypeDto SalaryType : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("日，月，年", SalaryType.getType());
            map.put(" low",  SalaryType.getLow());
            map.put(" high",  SalaryType.getHigh());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}