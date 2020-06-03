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

import me.zhengjie.api.domain.biz.School;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.api.repository.biz.SchoolRepository;
import me.zhengjie.modules.biz.service.SchoolService;
import me.zhengjie.modules.biz.service.dto.SchoolDto;
import me.zhengjie.modules.biz.service.dto.SchoolQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.SchoolMapper;
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
public class SchoolServiceImpl implements SchoolService {

    private final SchoolRepository SchoolRepository;
    private final SchoolMapper SchoolMapper;

    @Override
    public Map<String,Object> queryAll(SchoolQueryCriteria criteria, Pageable pageable){
        Page<School> page = SchoolRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(SchoolMapper::toDto));
    }

    @Override
    public List<SchoolDto> queryAll(SchoolQueryCriteria criteria){
        return SchoolMapper.toDto(SchoolRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public SchoolDto findById(Long id) {
        School School = SchoolRepository.findById(id).orElseGet(School::new);
        ValidationUtil.isNull(School.getId(),"School","id",id);
        return SchoolMapper.toDto(School);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SchoolDto create(School resources) {
        return SchoolMapper.toDto(SchoolRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(School resources) {
        School School = SchoolRepository.findById(resources.getId()).orElseGet(School::new);
        ValidationUtil.isNull( School.getId(),"School","id",resources.getId());
        BeanUtil.copyProperties(resources, School, CopyOptions.create().setIgnoreNullValue(true));
        SchoolRepository.save(School);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            SchoolRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<SchoolDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SchoolDto School : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" name",  School.getName());
            map.put(" cityId",  School.getCityId());
            map.put(" stateId",  School.getStateId());
            map.put(" districtId",  School.getDistrictId());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}