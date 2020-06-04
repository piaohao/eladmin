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

import me.zhengjie.api.domain.biz.ResumeAcademic;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.api.repository.biz.ResumeAcademicRepository;
import me.zhengjie.modules.biz.service.ResumeAcademicService;
import me.zhengjie.modules.biz.service.dto.ResumeAcademicDto;
import me.zhengjie.modules.biz.service.dto.ResumeAcademicQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.ResumeAcademicMapper;
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
public class ResumeAcademicServiceImpl implements ResumeAcademicService {

    private final ResumeAcademicRepository resumeAcademicRepository;
    private final ResumeAcademicMapper resumeAcademicMapper;

    @Override
    public Map<String,Object> queryAll(ResumeAcademicQueryCriteria criteria, Pageable pageable){
        Page<ResumeAcademic> page = resumeAcademicRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(resumeAcademicMapper::toDto));
    }

    @Override
    public List<ResumeAcademicDto> queryAll(ResumeAcademicQueryCriteria criteria){
        return resumeAcademicMapper.toDto(resumeAcademicRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public ResumeAcademicDto findById(Long id) {
        ResumeAcademic resumeAcademic = resumeAcademicRepository.findById(id).orElseGet(ResumeAcademic::new);
        ValidationUtil.isNull(resumeAcademic.getId(),"ResumeAcademic","id",id);
        return resumeAcademicMapper.toDto(resumeAcademic);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResumeAcademicDto create(ResumeAcademic resources) {
        return resumeAcademicMapper.toDto(resumeAcademicRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ResumeAcademic resources) {
        ResumeAcademic resumeAcademic = resumeAcademicRepository.findById(resources.getId()).orElseGet(ResumeAcademic::new);
        ValidationUtil.isNull( resumeAcademic.getId(),"ResumeAcademic","id",resources.getId());
        BeanUtil.copyProperties(resources, resumeAcademic, CopyOptions.create().setIgnoreNullValue(true));
        resumeAcademicRepository.save(resumeAcademic);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            resumeAcademicRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<ResumeAcademicDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ResumeAcademicDto resumeAcademic : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" resumeId",  resumeAcademic.getResumeId());
            map.put(" project",  resumeAcademic.getProject());
            map.put(" startTime",  resumeAcademic.getStartTime());
            map.put(" endTime",  resumeAcademic.getEndTime());
            map.put(" duty",  resumeAcademic.getDuty());
            map.put(" description",  resumeAcademic.getDescription());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}