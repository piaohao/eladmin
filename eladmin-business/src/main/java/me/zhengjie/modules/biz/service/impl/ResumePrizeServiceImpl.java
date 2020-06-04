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

import me.zhengjie.api.domain.biz.ResumePrize;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.api.repository.biz.ResumePrizeRepository;
import me.zhengjie.modules.biz.service.ResumePrizeService;
import me.zhengjie.modules.biz.service.dto.ResumePrizeDto;
import me.zhengjie.modules.biz.service.dto.ResumePrizeQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.ResumePrizeMapper;
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
public class ResumePrizeServiceImpl implements ResumePrizeService {

    private final ResumePrizeRepository resumePrizeRepository;
    private final ResumePrizeMapper resumePrizeMapper;

    @Override
    public Map<String,Object> queryAll(ResumePrizeQueryCriteria criteria, Pageable pageable){
        Page<ResumePrize> page = resumePrizeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(resumePrizeMapper::toDto));
    }

    @Override
    public List<ResumePrizeDto> queryAll(ResumePrizeQueryCriteria criteria){
        return resumePrizeMapper.toDto(resumePrizeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public ResumePrizeDto findById(Long id) {
        ResumePrize resumePrize = resumePrizeRepository.findById(id).orElseGet(ResumePrize::new);
        ValidationUtil.isNull(resumePrize.getId(),"ResumePrize","id",id);
        return resumePrizeMapper.toDto(resumePrize);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResumePrizeDto create(ResumePrize resources) {
        return resumePrizeMapper.toDto(resumePrizeRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ResumePrize resources) {
        ResumePrize resumePrize = resumePrizeRepository.findById(resources.getId()).orElseGet(ResumePrize::new);
        ValidationUtil.isNull( resumePrize.getId(),"ResumePrize","id",resources.getId());
        BeanUtil.copyProperties(resources, resumePrize, CopyOptions.create().setIgnoreNullValue(true));
        resumePrizeRepository.save(resumePrize);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            resumePrizeRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<ResumePrizeDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ResumePrizeDto resumePrize : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" resumeId",  resumePrize.getResumeId());
            map.put(" prizeName",  resumePrize.getPrizeName());
            map.put(" prizeTime",  resumePrize.getPrizeTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}