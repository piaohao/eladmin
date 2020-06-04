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

import me.zhengjie.api.domain.biz.ResumeIntention;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.api.repository.biz.ResumeIntentionRepository;
import me.zhengjie.modules.biz.service.ResumeIntentionService;
import me.zhengjie.modules.biz.service.dto.ResumeIntentionDto;
import me.zhengjie.modules.biz.service.dto.ResumeIntentionQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.ResumeIntentionMapper;
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
public class ResumeIntentionServiceImpl implements ResumeIntentionService {

    private final ResumeIntentionRepository resumeIntentionRepository;
    private final ResumeIntentionMapper resumeIntentionMapper;

    @Override
    public Map<String,Object> queryAll(ResumeIntentionQueryCriteria criteria, Pageable pageable){
        Page<ResumeIntention> page = resumeIntentionRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(resumeIntentionMapper::toDto));
    }

    @Override
    public List<ResumeIntentionDto> queryAll(ResumeIntentionQueryCriteria criteria){
        return resumeIntentionMapper.toDto(resumeIntentionRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public ResumeIntentionDto findById(Long id) {
        ResumeIntention resumeIntention = resumeIntentionRepository.findById(id).orElseGet(ResumeIntention::new);
        ValidationUtil.isNull(resumeIntention.getId(),"ResumeIntention","id",id);
        return resumeIntentionMapper.toDto(resumeIntention);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResumeIntentionDto create(ResumeIntention resources) {
        return resumeIntentionMapper.toDto(resumeIntentionRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ResumeIntention resources) {
        ResumeIntention resumeIntention = resumeIntentionRepository.findById(resources.getId()).orElseGet(ResumeIntention::new);
        ValidationUtil.isNull( resumeIntention.getId(),"ResumeIntention","id",resources.getId());
        BeanUtil.copyProperties(resources, resumeIntention, CopyOptions.create().setIgnoreNullValue(true));
        resumeIntentionRepository.save(resumeIntention);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            resumeIntentionRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<ResumeIntentionDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ResumeIntentionDto resumeIntention : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("职位类型id", resumeIntention.getVccTypeId());
            map.put("行业id", resumeIntention.getIndustryId());
            map.put("城市id", resumeIntention.getCityId());
            map.put("求职性质,campus,intern,all", resumeIntention.getVccProperty());
            map.put(" studentUserId",  resumeIntention.getStudentUserId());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}