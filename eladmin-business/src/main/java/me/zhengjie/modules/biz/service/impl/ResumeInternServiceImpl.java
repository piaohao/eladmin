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
import me.zhengjie.api.domain.biz.ResumeIntern;
import me.zhengjie.api.repository.biz.ResumeInternRepository;
import me.zhengjie.modules.biz.service.ResumeInternService;
import me.zhengjie.modules.biz.service.dto.ResumeInternDto;
import me.zhengjie.modules.biz.service.dto.ResumeInternQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.ResumeInternMapper;
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
public class ResumeInternServiceImpl implements ResumeInternService {

    private final ResumeInternRepository ResumeInternRepository;
    private final ResumeInternMapper ResumeInternMapper;

    @Override
    public Map<String,Object> queryAll(ResumeInternQueryCriteria criteria, Pageable pageable){
        Page<ResumeIntern> page = ResumeInternRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(ResumeInternMapper::toDto));
    }

    @Override
    public List<ResumeInternDto> queryAll(ResumeInternQueryCriteria criteria){
        return ResumeInternMapper.toDto(ResumeInternRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public ResumeInternDto findById(Long id) {
        ResumeIntern ResumeIntern = ResumeInternRepository.findById(id).orElseGet(ResumeIntern::new);
        ValidationUtil.isNull(ResumeIntern.getId(),"ResumeIntern","id",id);
        return ResumeInternMapper.toDto(ResumeIntern);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResumeInternDto create(ResumeIntern resources) {
        return ResumeInternMapper.toDto(ResumeInternRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ResumeIntern resources) {
        ResumeIntern ResumeIntern = ResumeInternRepository.findById(resources.getId()).orElseGet(ResumeIntern::new);
        ValidationUtil.isNull( ResumeIntern.getId(),"ResumeIntern","id",resources.getId());
        BeanUtil.copyProperties(resources, ResumeIntern, CopyOptions.create().setIgnoreNullValue(true));
        ResumeInternRepository.save(ResumeIntern);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            ResumeInternRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<ResumeInternDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ResumeInternDto ResumeIntern : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" resumeId",  ResumeIntern.getResumeId());
            map.put(" company",  ResumeIntern.getCompany());
            map.put(" industry",  ResumeIntern.getIndustry());
            map.put(" entryTime",  ResumeIntern.getEntryTime());
            map.put(" resignationTime",  ResumeIntern.getResignationTime());
            map.put(" position",  ResumeIntern.getPosition());
            map.put(" jobContent",  ResumeIntern.getJobContent());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}