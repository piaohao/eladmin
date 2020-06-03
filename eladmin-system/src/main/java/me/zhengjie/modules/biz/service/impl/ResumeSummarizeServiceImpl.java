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
public class ResumeSummarizeServiceImpl implements ResumeSummarizeService {

    private final ResumeSummarizeRepository ResumeSummarizeRepository;
    private final ResumeSummarizeMapper ResumeSummarizeMapper;

    @Override
    public Map<String,Object> queryAll(ResumeSummarizeQueryCriteria criteria, Pageable pageable){
        Page<ResumeSummarize> page = ResumeSummarizeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(ResumeSummarizeMapper::toDto));
    }

    @Override
    public List<ResumeSummarizeDto> queryAll(ResumeSummarizeQueryCriteria criteria){
        return ResumeSummarizeMapper.toDto(ResumeSummarizeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public ResumeSummarizeDto findById(Long id) {
        ResumeSummarize ResumeSummarize = ResumeSummarizeRepository.findById(id).orElseGet(ResumeSummarize::new);
        ValidationUtil.isNull(ResumeSummarize.getId(),"ResumeSummarize","id",id);
        return ResumeSummarizeMapper.toDto(ResumeSummarize);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResumeSummarizeDto create(ResumeSummarize resources) {
        return ResumeSummarizeMapper.toDto(ResumeSummarizeRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ResumeSummarize resources) {
        ResumeSummarize ResumeSummarize = ResumeSummarizeRepository.findById(resources.getId()).orElseGet(ResumeSummarize::new);
        ValidationUtil.isNull( ResumeSummarize.getId(),"ResumeSummarize","id",resources.getId());
        BeanUtil.copyProperties(resources, ResumeSummarize, CopyOptions.create().setIgnoreNullValue(true));
        ResumeSummarizeRepository.save(ResumeSummarize);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            ResumeSummarizeRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<ResumeSummarizeDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ResumeSummarizeDto ResumeSummarize : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" resumeId",  ResumeSummarize.getResumeId());
            map.put(" content",  ResumeSummarize.getContent());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}