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
import me.zhengjie.api.domain.biz.ResumeAcademic;
import me.zhengjie.api.repository.biz.ResumeAcademicRepository;
import me.zhengjie.modules.biz.service.ResumeAcademicService;
import me.zhengjie.modules.biz.service.dto.ResumeAcademicDto;
import me.zhengjie.modules.biz.service.dto.ResumeAcademicQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.ResumeAcademicMapper;
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
public class ResumeAcademicServiceImpl implements ResumeAcademicService {

    private final ResumeAcademicRepository ResumeAcademicRepository;
    private final ResumeAcademicMapper ResumeAcademicMapper;

    @Override
    public Map<String,Object> queryAll(ResumeAcademicQueryCriteria criteria, Pageable pageable){
        Page<ResumeAcademic> page = ResumeAcademicRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(ResumeAcademicMapper::toDto));
    }

    @Override
    public List<ResumeAcademicDto> queryAll(ResumeAcademicQueryCriteria criteria){
        return ResumeAcademicMapper.toDto(ResumeAcademicRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public ResumeAcademicDto findById(Long id) {
        ResumeAcademic ResumeAcademic = ResumeAcademicRepository.findById(id).orElseGet(ResumeAcademic::new);
        ValidationUtil.isNull(ResumeAcademic.getId(),"ResumeAcademic","id",id);
        return ResumeAcademicMapper.toDto(ResumeAcademic);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResumeAcademicDto create(ResumeAcademic resources) {
        return ResumeAcademicMapper.toDto(ResumeAcademicRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ResumeAcademic resources) {
        ResumeAcademic ResumeAcademic = ResumeAcademicRepository.findById(resources.getId()).orElseGet(ResumeAcademic::new);
        ValidationUtil.isNull( ResumeAcademic.getId(),"ResumeAcademic","id",resources.getId());
        BeanUtil.copyProperties(resources, ResumeAcademic, CopyOptions.create().setIgnoreNullValue(true));
        ResumeAcademicRepository.save(ResumeAcademic);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            ResumeAcademicRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<ResumeAcademicDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ResumeAcademicDto ResumeAcademic : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" resumeId",  ResumeAcademic.getResumeId());
            map.put(" project",  ResumeAcademic.getProject());
            map.put(" startTime",  ResumeAcademic.getStartTime());
            map.put(" endTime",  ResumeAcademic.getEndTime());
            map.put(" duty",  ResumeAcademic.getDuty());
            map.put(" description",  ResumeAcademic.getDescription());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}