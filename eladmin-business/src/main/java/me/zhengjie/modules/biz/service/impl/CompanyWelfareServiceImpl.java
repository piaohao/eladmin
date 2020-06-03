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
import me.zhengjie.api.domain.biz.CompanyWelfare;
import me.zhengjie.api.repository.biz.CompanyWelfareRepository;
import me.zhengjie.modules.biz.service.CompanyWelfareService;
import me.zhengjie.modules.biz.service.dto.CompanyWelfareDto;
import me.zhengjie.modules.biz.service.dto.CompanyWelfareQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.CompanyWelfareMapper;
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
public class CompanyWelfareServiceImpl implements CompanyWelfareService {

    private final CompanyWelfareRepository CompanyWelfareRepository;
    private final CompanyWelfareMapper CompanyWelfareMapper;

    @Override
    public Map<String,Object> queryAll(CompanyWelfareQueryCriteria criteria, Pageable pageable){
        Page<CompanyWelfare> page = CompanyWelfareRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(CompanyWelfareMapper::toDto));
    }

    @Override
    public List<CompanyWelfareDto> queryAll(CompanyWelfareQueryCriteria criteria){
        return CompanyWelfareMapper.toDto(CompanyWelfareRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public CompanyWelfareDto findById(Long id) {
        CompanyWelfare CompanyWelfare = CompanyWelfareRepository.findById(id).orElseGet(CompanyWelfare::new);
        ValidationUtil.isNull(CompanyWelfare.getId(),"CompanyWelfare","id",id);
        return CompanyWelfareMapper.toDto(CompanyWelfare);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CompanyWelfareDto create(CompanyWelfare resources) {
        return CompanyWelfareMapper.toDto(CompanyWelfareRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(CompanyWelfare resources) {
        CompanyWelfare CompanyWelfare = CompanyWelfareRepository.findById(resources.getId()).orElseGet(CompanyWelfare::new);
        ValidationUtil.isNull( CompanyWelfare.getId(),"CompanyWelfare","id",resources.getId());
        BeanUtil.copyProperties(resources, CompanyWelfare, CopyOptions.create().setIgnoreNullValue(true));
        CompanyWelfareRepository.save(CompanyWelfare);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            CompanyWelfareRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<CompanyWelfareDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (CompanyWelfareDto CompanyWelfare : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" name",  CompanyWelfare.getName());
            map.put(" createdAt",  CompanyWelfare.getCreatedAt());
            map.put(" updatedAt",  CompanyWelfare.getUpdatedAt());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}