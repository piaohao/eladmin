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

import me.zhengjie.api.domain.biz.ResumeSummarize;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.api.repository.biz.ResumeSummarizeRepository;
import me.zhengjie.modules.biz.service.ResumeSummarizeService;
import me.zhengjie.modules.biz.service.dto.ResumeSummarizeDto;
import me.zhengjie.modules.biz.service.dto.ResumeSummarizeQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.ResumeSummarizeMapper;
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
public class ResumeSummarizeServiceImpl implements ResumeSummarizeService {

    private final ResumeSummarizeRepository resumeSummarizeRepository;
    private final ResumeSummarizeMapper resumeSummarizeMapper;

    @Override
    public Map<String,Object> queryAll(ResumeSummarizeQueryCriteria criteria, Pageable pageable){
        Page<ResumeSummarize> page = resumeSummarizeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(resumeSummarizeMapper::toDto));
    }

    @Override
    public List<ResumeSummarizeDto> queryAll(ResumeSummarizeQueryCriteria criteria){
        return resumeSummarizeMapper.toDto(resumeSummarizeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public ResumeSummarizeDto findById(Long id) {
        ResumeSummarize resumeSummarize = resumeSummarizeRepository.findById(id).orElseGet(ResumeSummarize::new);
        ValidationUtil.isNull(resumeSummarize.getId(),"ResumeSummarize","id",id);
        return resumeSummarizeMapper.toDto(resumeSummarize);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResumeSummarizeDto create(ResumeSummarize resources) {
        return resumeSummarizeMapper.toDto(resumeSummarizeRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ResumeSummarize resources) {
        ResumeSummarize resumeSummarize = resumeSummarizeRepository.findById(resources.getId()).orElseGet(ResumeSummarize::new);
        ValidationUtil.isNull( resumeSummarize.getId(),"ResumeSummarize","id",resources.getId());
        BeanUtil.copyProperties(resources, resumeSummarize, CopyOptions.create().setIgnoreNullValue(true));
        resumeSummarizeRepository.save(resumeSummarize);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            resumeSummarizeRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<ResumeSummarizeDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ResumeSummarizeDto resumeSummarize : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" resumeId",  resumeSummarize.getResumeId());
            map.put(" content",  resumeSummarize.getContent());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}