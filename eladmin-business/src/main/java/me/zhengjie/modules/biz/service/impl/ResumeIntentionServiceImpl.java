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
import me.zhengjie.api.domain.biz.ResumeIntention;
import me.zhengjie.api.repository.biz.ResumeIntentionRepository;
import me.zhengjie.modules.biz.service.ResumeIntentionService;
import me.zhengjie.modules.biz.service.dto.ResumeIntentionDto;
import me.zhengjie.modules.biz.service.dto.ResumeIntentionQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.ResumeIntentionMapper;
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
public class ResumeIntentionServiceImpl implements ResumeIntentionService {

    private final ResumeIntentionRepository ResumeIntentionRepository;
    private final ResumeIntentionMapper ResumeIntentionMapper;

    @Override
    public Map<String,Object> queryAll(ResumeIntentionQueryCriteria criteria, Pageable pageable){
        Page<ResumeIntention> page = ResumeIntentionRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(ResumeIntentionMapper::toDto));
    }

    @Override
    public List<ResumeIntentionDto> queryAll(ResumeIntentionQueryCriteria criteria){
        return ResumeIntentionMapper.toDto(ResumeIntentionRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public ResumeIntentionDto findById(Long id) {
        ResumeIntention ResumeIntention = ResumeIntentionRepository.findById(id).orElseGet(ResumeIntention::new);
        ValidationUtil.isNull(ResumeIntention.getId(),"ResumeIntention","id",id);
        return ResumeIntentionMapper.toDto(ResumeIntention);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResumeIntentionDto create(ResumeIntention resources) {
        return ResumeIntentionMapper.toDto(ResumeIntentionRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ResumeIntention resources) {
        ResumeIntention ResumeIntention = ResumeIntentionRepository.findById(resources.getId()).orElseGet(ResumeIntention::new);
        ValidationUtil.isNull( ResumeIntention.getId(),"ResumeIntention","id",resources.getId());
        BeanUtil.copyProperties(resources, ResumeIntention, CopyOptions.create().setIgnoreNullValue(true));
        ResumeIntentionRepository.save(ResumeIntention);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            ResumeIntentionRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<ResumeIntentionDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ResumeIntentionDto ResumeIntention : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("职位类型id", ResumeIntention.getVccTypeId());
            map.put("行业id", ResumeIntention.getIndustryId());
            map.put("城市id", ResumeIntention.getCityId());
            map.put("求职性质,campus,intern,all", ResumeIntention.getVccProperty());
            map.put(" studentUserId",  ResumeIntention.getStudentUserId());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}