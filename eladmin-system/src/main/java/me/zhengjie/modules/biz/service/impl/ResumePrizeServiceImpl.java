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
public class ResumePrizeServiceImpl implements ResumePrizeService {

    private final ResumePrizeRepository ResumePrizeRepository;
    private final ResumePrizeMapper ResumePrizeMapper;

    @Override
    public Map<String,Object> queryAll(ResumePrizeQueryCriteria criteria, Pageable pageable){
        Page<ResumePrize> page = ResumePrizeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(ResumePrizeMapper::toDto));
    }

    @Override
    public List<ResumePrizeDto> queryAll(ResumePrizeQueryCriteria criteria){
        return ResumePrizeMapper.toDto(ResumePrizeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public ResumePrizeDto findById(Long id) {
        ResumePrize ResumePrize = ResumePrizeRepository.findById(id).orElseGet(ResumePrize::new);
        ValidationUtil.isNull(ResumePrize.getId(),"ResumePrize","id",id);
        return ResumePrizeMapper.toDto(ResumePrize);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResumePrizeDto create(ResumePrize resources) {
        return ResumePrizeMapper.toDto(ResumePrizeRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ResumePrize resources) {
        ResumePrize ResumePrize = ResumePrizeRepository.findById(resources.getId()).orElseGet(ResumePrize::new);
        ValidationUtil.isNull( ResumePrize.getId(),"ResumePrize","id",resources.getId());
        BeanUtil.copyProperties(resources, ResumePrize, CopyOptions.create().setIgnoreNullValue(true));
        ResumePrizeRepository.save(ResumePrize);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            ResumePrizeRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<ResumePrizeDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ResumePrizeDto ResumePrize : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" resumeId",  ResumePrize.getResumeId());
            map.put(" prizeName",  ResumePrize.getPrizeName());
            map.put(" prizeTime",  ResumePrize.getPrizeTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}