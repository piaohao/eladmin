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
public class CollegeServiceImpl implements CollegeService {

    private final CollegeRepository CollegeRepository;
    private final CollegeMapper CollegeMapper;

    @Override
    public Map<String,Object> queryAll(CollegeQueryCriteria criteria, Pageable pageable){
        Page<College> page = CollegeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(CollegeMapper::toDto));
    }

    @Override
    public List<CollegeDto> queryAll(CollegeQueryCriteria criteria){
        return CollegeMapper.toDto(CollegeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public CollegeDto findById(Long id) {
        College College = CollegeRepository.findById(id).orElseGet(College::new);
        ValidationUtil.isNull(College.getId(),"College","id",id);
        return CollegeMapper.toDto(College);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CollegeDto create(College resources) {
        return CollegeMapper.toDto(CollegeRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(College resources) {
        College College = CollegeRepository.findById(resources.getId()).orElseGet(College::new);
        ValidationUtil.isNull( College.getId(),"College","id",resources.getId());
        BeanUtil.copyProperties(resources, College, CopyOptions.create().setIgnoreNullValue(true));
        CollegeRepository.save(College);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            CollegeRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<CollegeDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (CollegeDto College : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" schoolId",  College.getSchoolId());
            map.put(" name",  College.getName());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}