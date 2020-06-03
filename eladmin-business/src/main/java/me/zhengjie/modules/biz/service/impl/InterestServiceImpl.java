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
import me.zhengjie.api.domain.biz.Interest;
import me.zhengjie.api.repository.biz.InterestRepository;
import me.zhengjie.modules.biz.service.InterestService;
import me.zhengjie.modules.biz.service.dto.InterestDto;
import me.zhengjie.modules.biz.service.dto.InterestQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.InterestMapper;
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
public class InterestServiceImpl implements InterestService {

    private final InterestRepository InterestRepository;
    private final InterestMapper InterestMapper;

    @Override
    public Map<String,Object> queryAll(InterestQueryCriteria criteria, Pageable pageable){
        Page<Interest> page = InterestRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(InterestMapper::toDto));
    }

    @Override
    public List<InterestDto> queryAll(InterestQueryCriteria criteria){
        return InterestMapper.toDto(InterestRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public InterestDto findById(Long id) {
        Interest Interest = InterestRepository.findById(id).orElseGet(Interest::new);
        ValidationUtil.isNull(Interest.getId(),"Interest","id",id);
        return InterestMapper.toDto(Interest);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public InterestDto create(Interest resources) {
        return InterestMapper.toDto(InterestRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Interest resources) {
        Interest Interest = InterestRepository.findById(resources.getId()).orElseGet(Interest::new);
        ValidationUtil.isNull( Interest.getId(),"Interest","id",resources.getId());
        BeanUtil.copyProperties(resources, Interest, CopyOptions.create().setIgnoreNullValue(true));
        InterestRepository.save(Interest);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            InterestRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<InterestDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (InterestDto Interest : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("兴趣", Interest.getName());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}