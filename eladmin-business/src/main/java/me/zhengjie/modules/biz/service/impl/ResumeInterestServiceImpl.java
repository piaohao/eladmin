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
import me.zhengjie.api.domain.biz.ResumeInterest;
import me.zhengjie.api.repository.biz.ResumeInterestRepository;
import me.zhengjie.modules.biz.service.ResumeInterestService;
import me.zhengjie.modules.biz.service.dto.ResumeInterestDto;
import me.zhengjie.modules.biz.service.dto.ResumeInterestQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.ResumeInterestMapper;
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
public class ResumeInterestServiceImpl implements ResumeInterestService {

    private final ResumeInterestRepository ResumeInterestRepository;
    private final ResumeInterestMapper ResumeInterestMapper;

    @Override
    public Map<String,Object> queryAll(ResumeInterestQueryCriteria criteria, Pageable pageable){
        Page<ResumeInterest> page = ResumeInterestRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(ResumeInterestMapper::toDto));
    }

    @Override
    public List<ResumeInterestDto> queryAll(ResumeInterestQueryCriteria criteria){
        return ResumeInterestMapper.toDto(ResumeInterestRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public ResumeInterestDto findById(Long id) {
        ResumeInterest ResumeInterest = ResumeInterestRepository.findById(id).orElseGet(ResumeInterest::new);
        ValidationUtil.isNull(ResumeInterest.getId(),"ResumeInterest","id",id);
        return ResumeInterestMapper.toDto(ResumeInterest);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResumeInterestDto create(ResumeInterest resources) {
        return ResumeInterestMapper.toDto(ResumeInterestRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ResumeInterest resources) {
        ResumeInterest ResumeInterest = ResumeInterestRepository.findById(resources.getId()).orElseGet(ResumeInterest::new);
        ValidationUtil.isNull( ResumeInterest.getId(),"ResumeInterest","id",resources.getId());
        BeanUtil.copyProperties(resources, ResumeInterest, CopyOptions.create().setIgnoreNullValue(true));
        ResumeInterestRepository.save(ResumeInterest);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            ResumeInterestRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<ResumeInterestDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ResumeInterestDto ResumeInterest : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" resumeId",  ResumeInterest.getResumeId());
            map.put(" tags",  ResumeInterest.getTags());
            map.put(" custom",  ResumeInterest.getCustom());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}