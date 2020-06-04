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

import me.zhengjie.api.domain.biz.Interest;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.api.repository.biz.InterestRepository;
import me.zhengjie.modules.biz.service.InterestService;
import me.zhengjie.modules.biz.service.dto.InterestDto;
import me.zhengjie.modules.biz.service.dto.InterestQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.InterestMapper;
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
public class InterestServiceImpl implements InterestService {

    private final InterestRepository interestRepository;
    private final InterestMapper interestMapper;

    @Override
    public Map<String,Object> queryAll(InterestQueryCriteria criteria, Pageable pageable){
        Page<Interest> page = interestRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(interestMapper::toDto));
    }

    @Override
    public List<InterestDto> queryAll(InterestQueryCriteria criteria){
        return interestMapper.toDto(interestRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public InterestDto findById(Long id) {
        Interest interest = interestRepository.findById(id).orElseGet(Interest::new);
        ValidationUtil.isNull(interest.getId(),"Interest","id",id);
        return interestMapper.toDto(interest);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public InterestDto create(Interest resources) {
        return interestMapper.toDto(interestRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Interest resources) {
        Interest interest = interestRepository.findById(resources.getId()).orElseGet(Interest::new);
        ValidationUtil.isNull( interest.getId(),"Interest","id",resources.getId());
        BeanUtil.copyProperties(resources, interest, CopyOptions.create().setIgnoreNullValue(true));
        interestRepository.save(interest);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            interestRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<InterestDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (InterestDto interest : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("兴趣", interest.getName());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}