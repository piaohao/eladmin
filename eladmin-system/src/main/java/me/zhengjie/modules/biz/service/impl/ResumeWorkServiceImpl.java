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
* @date 2020-06-03
**/
@Service
@RequiredArgsConstructor
public class ResumeWorkServiceImpl implements ResumeWorkService {

    private final ResumeWorkRepository ResumeWorkRepository;
    private final ResumeWorkMapper ResumeWorkMapper;

    @Override
    public Map<String,Object> queryAll(ResumeWorkQueryCriteria criteria, Pageable pageable){
        Page<ResumeWork> page = ResumeWorkRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(ResumeWorkMapper::toDto));
    }

    @Override
    public List<ResumeWorkDto> queryAll(ResumeWorkQueryCriteria criteria){
        return ResumeWorkMapper.toDto(ResumeWorkRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public ResumeWorkDto findById(Long id) {
        ResumeWork ResumeWork = ResumeWorkRepository.findById(id).orElseGet(ResumeWork::new);
        ValidationUtil.isNull(ResumeWork.getId(),"ResumeWork","id",id);
        return ResumeWorkMapper.toDto(ResumeWork);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResumeWorkDto create(ResumeWork resources) {
        return ResumeWorkMapper.toDto(ResumeWorkRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ResumeWork resources) {
        ResumeWork ResumeWork = ResumeWorkRepository.findById(resources.getId()).orElseGet(ResumeWork::new);
        ValidationUtil.isNull( ResumeWork.getId(),"ResumeWork","id",resources.getId());
        BeanUtil.copyProperties(resources, ResumeWork, CopyOptions.create().setIgnoreNullValue(true));
        ResumeWorkRepository.save(ResumeWork);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            ResumeWorkRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<ResumeWorkDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ResumeWorkDto ResumeWork : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" resumeId",  ResumeWork.getResumeId());
            map.put(" workName",  ResumeWork.getWorkName());
            map.put(" link",  ResumeWork.getLink());
            map.put(" description",  ResumeWork.getDescription());
            map.put(" photos",  ResumeWork.getPhotos());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}