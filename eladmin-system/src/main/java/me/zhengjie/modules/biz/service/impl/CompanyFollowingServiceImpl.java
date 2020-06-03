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
public class CompanyFollowingServiceImpl implements CompanyFollowingService {

    private final CompanyFollowingRepository CompanyFollowingRepository;
    private final CompanyFollowingMapper CompanyFollowingMapper;

    @Override
    public Map<String,Object> queryAll(CompanyFollowingQueryCriteria criteria, Pageable pageable){
        Page<CompanyFollowing> page = CompanyFollowingRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(CompanyFollowingMapper::toDto));
    }

    @Override
    public List<CompanyFollowingDto> queryAll(CompanyFollowingQueryCriteria criteria){
        return CompanyFollowingMapper.toDto(CompanyFollowingRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public CompanyFollowingDto findById(Long id) {
        CompanyFollowing CompanyFollowing = CompanyFollowingRepository.findById(id).orElseGet(CompanyFollowing::new);
        ValidationUtil.isNull(CompanyFollowing.getId(),"CompanyFollowing","id",id);
        return CompanyFollowingMapper.toDto(CompanyFollowing);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CompanyFollowingDto create(CompanyFollowing resources) {
        return CompanyFollowingMapper.toDto(CompanyFollowingRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(CompanyFollowing resources) {
        CompanyFollowing CompanyFollowing = CompanyFollowingRepository.findById(resources.getId()).orElseGet(CompanyFollowing::new);
        ValidationUtil.isNull( CompanyFollowing.getId(),"CompanyFollowing","id",resources.getId());
        BeanUtil.copyProperties(resources, CompanyFollowing, CopyOptions.create().setIgnoreNullValue(true));
        CompanyFollowingRepository.save(CompanyFollowing);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            CompanyFollowingRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<CompanyFollowingDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (CompanyFollowingDto CompanyFollowing : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" companyId",  CompanyFollowing.getCompanyId());
            map.put(" studentUserId",  CompanyFollowing.getStudentUserId());
            map.put(" createdAt",  CompanyFollowing.getCreatedAt());
            map.put(" updatedAt",  CompanyFollowing.getUpdatedAt());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}