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

import me.zhengjie.api.domain.biz.College;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.api.repository.biz.CollegeRepository;
import me.zhengjie.modules.biz.service.CollegeService;
import me.zhengjie.modules.biz.service.dto.CollegeDto;
import me.zhengjie.modules.biz.service.dto.CollegeQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.CollegeMapper;
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
public class CollegeServiceImpl implements CollegeService {

    private final CollegeRepository collegeRepository;
    private final CollegeMapper collegeMapper;

    @Override
    public Map<String,Object> queryAll(CollegeQueryCriteria criteria, Pageable pageable){
        Page<College> page = collegeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(collegeMapper::toDto));
    }

    @Override
    public List<CollegeDto> queryAll(CollegeQueryCriteria criteria){
        return collegeMapper.toDto(collegeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public CollegeDto findById(Long id) {
        College college = collegeRepository.findById(id).orElseGet(College::new);
        ValidationUtil.isNull(college.getId(),"College","id",id);
        return collegeMapper.toDto(college);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CollegeDto create(College resources) {
        return collegeMapper.toDto(collegeRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(College resources) {
        College college = collegeRepository.findById(resources.getId()).orElseGet(College::new);
        ValidationUtil.isNull( college.getId(),"College","id",resources.getId());
        BeanUtil.copyProperties(resources, college, CopyOptions.create().setIgnoreNullValue(true));
        collegeRepository.save(college);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            collegeRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<CollegeDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (CollegeDto college : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" schoolId",  college.getSchoolId());
            map.put(" name",  college.getName());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}