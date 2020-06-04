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

import me.zhengjie.api.domain.biz.ResumeWork;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.api.repository.biz.ResumeWorkRepository;
import me.zhengjie.modules.biz.service.ResumeWorkService;
import me.zhengjie.modules.biz.service.dto.ResumeWorkDto;
import me.zhengjie.modules.biz.service.dto.ResumeWorkQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.ResumeWorkMapper;
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
public class ResumeWorkServiceImpl implements ResumeWorkService {

    private final ResumeWorkRepository resumeWorkRepository;
    private final ResumeWorkMapper resumeWorkMapper;

    @Override
    public Map<String,Object> queryAll(ResumeWorkQueryCriteria criteria, Pageable pageable){
        Page<ResumeWork> page = resumeWorkRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(resumeWorkMapper::toDto));
    }

    @Override
    public List<ResumeWorkDto> queryAll(ResumeWorkQueryCriteria criteria){
        return resumeWorkMapper.toDto(resumeWorkRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public ResumeWorkDto findById(Long id) {
        ResumeWork resumeWork = resumeWorkRepository.findById(id).orElseGet(ResumeWork::new);
        ValidationUtil.isNull(resumeWork.getId(),"ResumeWork","id",id);
        return resumeWorkMapper.toDto(resumeWork);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResumeWorkDto create(ResumeWork resources) {
        return resumeWorkMapper.toDto(resumeWorkRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ResumeWork resources) {
        ResumeWork resumeWork = resumeWorkRepository.findById(resources.getId()).orElseGet(ResumeWork::new);
        ValidationUtil.isNull( resumeWork.getId(),"ResumeWork","id",resources.getId());
        BeanUtil.copyProperties(resources, resumeWork, CopyOptions.create().setIgnoreNullValue(true));
        resumeWorkRepository.save(resumeWork);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            resumeWorkRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<ResumeWorkDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ResumeWorkDto resumeWork : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" resumeId",  resumeWork.getResumeId());
            map.put(" workName",  resumeWork.getWorkName());
            map.put(" link",  resumeWork.getLink());
            map.put(" description",  resumeWork.getDescription());
            map.put(" photos",  resumeWork.getPhotos());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}