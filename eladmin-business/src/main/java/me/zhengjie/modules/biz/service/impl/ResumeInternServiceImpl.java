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

import me.zhengjie.api.domain.biz.ResumeIntern;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.api.repository.biz.ResumeInternRepository;
import me.zhengjie.modules.biz.service.ResumeInternService;
import me.zhengjie.modules.biz.service.dto.ResumeInternDto;
import me.zhengjie.modules.biz.service.dto.ResumeInternQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.ResumeInternMapper;
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
public class ResumeInternServiceImpl implements ResumeInternService {

    private final ResumeInternRepository resumeInternRepository;
    private final ResumeInternMapper resumeInternMapper;

    @Override
    public Map<String,Object> queryAll(ResumeInternQueryCriteria criteria, Pageable pageable){
        Page<ResumeIntern> page = resumeInternRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(resumeInternMapper::toDto));
    }

    @Override
    public List<ResumeInternDto> queryAll(ResumeInternQueryCriteria criteria){
        return resumeInternMapper.toDto(resumeInternRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public ResumeInternDto findById(Long id) {
        ResumeIntern resumeIntern = resumeInternRepository.findById(id).orElseGet(ResumeIntern::new);
        ValidationUtil.isNull(resumeIntern.getId(),"ResumeIntern","id",id);
        return resumeInternMapper.toDto(resumeIntern);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResumeInternDto create(ResumeIntern resources) {
        return resumeInternMapper.toDto(resumeInternRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ResumeIntern resources) {
        ResumeIntern resumeIntern = resumeInternRepository.findById(resources.getId()).orElseGet(ResumeIntern::new);
        ValidationUtil.isNull( resumeIntern.getId(),"ResumeIntern","id",resources.getId());
        BeanUtil.copyProperties(resources, resumeIntern, CopyOptions.create().setIgnoreNullValue(true));
        resumeInternRepository.save(resumeIntern);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            resumeInternRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<ResumeInternDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ResumeInternDto resumeIntern : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" resumeId",  resumeIntern.getResumeId());
            map.put(" company",  resumeIntern.getCompany());
            map.put(" industry",  resumeIntern.getIndustry());
            map.put(" entryTime",  resumeIntern.getEntryTime());
            map.put(" resignationTime",  resumeIntern.getResignationTime());
            map.put(" position",  resumeIntern.getPosition());
            map.put(" jobContent",  resumeIntern.getJobContent());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}