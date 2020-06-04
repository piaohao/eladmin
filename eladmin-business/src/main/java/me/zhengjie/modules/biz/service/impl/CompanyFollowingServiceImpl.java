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

import me.zhengjie.api.domain.biz.CompanyFollowing;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.api.repository.biz.CompanyFollowingRepository;
import me.zhengjie.modules.biz.service.CompanyFollowingService;
import me.zhengjie.modules.biz.service.dto.CompanyFollowingDto;
import me.zhengjie.modules.biz.service.dto.CompanyFollowingQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.CompanyFollowingMapper;
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
public class CompanyFollowingServiceImpl implements CompanyFollowingService {

    private final CompanyFollowingRepository companyFollowingRepository;
    private final CompanyFollowingMapper companyFollowingMapper;

    @Override
    public Map<String,Object> queryAll(CompanyFollowingQueryCriteria criteria, Pageable pageable){
        Page<CompanyFollowing> page = companyFollowingRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(companyFollowingMapper::toDto));
    }

    @Override
    public List<CompanyFollowingDto> queryAll(CompanyFollowingQueryCriteria criteria){
        return companyFollowingMapper.toDto(companyFollowingRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public CompanyFollowingDto findById(Long id) {
        CompanyFollowing companyFollowing = companyFollowingRepository.findById(id).orElseGet(CompanyFollowing::new);
        ValidationUtil.isNull(companyFollowing.getId(),"CompanyFollowing","id",id);
        return companyFollowingMapper.toDto(companyFollowing);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CompanyFollowingDto create(CompanyFollowing resources) {
        return companyFollowingMapper.toDto(companyFollowingRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(CompanyFollowing resources) {
        CompanyFollowing companyFollowing = companyFollowingRepository.findById(resources.getId()).orElseGet(CompanyFollowing::new);
        ValidationUtil.isNull( companyFollowing.getId(),"CompanyFollowing","id",resources.getId());
        BeanUtil.copyProperties(resources, companyFollowing, CopyOptions.create().setIgnoreNullValue(true));
        companyFollowingRepository.save(companyFollowing);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            companyFollowingRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<CompanyFollowingDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (CompanyFollowingDto companyFollowing : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" companyId",  companyFollowing.getCompanyId());
            map.put(" studentUserId",  companyFollowing.getStudentUserId());
            map.put(" createdAt",  companyFollowing.getCreatedAt());
            map.put(" updatedAt",  companyFollowing.getUpdatedAt());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}