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
public class MajorServiceImpl implements MajorService {

    private final MajorRepository MajorRepository;
    private final MajorMapper MajorMapper;

    @Override
    public Map<String,Object> queryAll(MajorQueryCriteria criteria, Pageable pageable){
        Page<Major> page = MajorRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(MajorMapper::toDto));
    }

    @Override
    public List<MajorDto> queryAll(MajorQueryCriteria criteria){
        return MajorMapper.toDto(MajorRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public MajorDto findById(Long id) {
        Major Major = MajorRepository.findById(id).orElseGet(Major::new);
        ValidationUtil.isNull(Major.getId(),"Major","id",id);
        return MajorMapper.toDto(Major);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MajorDto create(Major resources) {
        return MajorMapper.toDto(MajorRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Major resources) {
        Major Major = MajorRepository.findById(resources.getId()).orElseGet(Major::new);
        ValidationUtil.isNull( Major.getId(),"Major","id",resources.getId());
        BeanUtil.copyProperties(resources, Major, CopyOptions.create().setIgnoreNullValue(true));
        MajorRepository.save(Major);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            MajorRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<MajorDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (MajorDto Major : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("专业名称", Major.getName());
            map.put("学院id", Major.getCollegeId());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}