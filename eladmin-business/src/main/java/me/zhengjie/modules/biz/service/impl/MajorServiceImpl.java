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

import me.zhengjie.api.domain.biz.Major;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.api.repository.biz.MajorRepository;
import me.zhengjie.modules.biz.service.MajorService;
import me.zhengjie.modules.biz.service.dto.MajorDto;
import me.zhengjie.modules.biz.service.dto.MajorQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.MajorMapper;
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
public class MajorServiceImpl implements MajorService {

    private final MajorRepository majorRepository;
    private final MajorMapper majorMapper;

    @Override
    public Map<String,Object> queryAll(MajorQueryCriteria criteria, Pageable pageable){
        Page<Major> page = majorRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(majorMapper::toDto));
    }

    @Override
    public List<MajorDto> queryAll(MajorQueryCriteria criteria){
        return majorMapper.toDto(majorRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public MajorDto findById(Long id) {
        Major major = majorRepository.findById(id).orElseGet(Major::new);
        ValidationUtil.isNull(major.getId(),"Major","id",id);
        return majorMapper.toDto(major);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MajorDto create(Major resources) {
        return majorMapper.toDto(majorRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Major resources) {
        Major major = majorRepository.findById(resources.getId()).orElseGet(Major::new);
        ValidationUtil.isNull( major.getId(),"Major","id",resources.getId());
        BeanUtil.copyProperties(resources, major, CopyOptions.create().setIgnoreNullValue(true));
        majorRepository.save(major);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            majorRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<MajorDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (MajorDto major : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("专业名称", major.getName());
            map.put("学院id", major.getCollegeId());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}