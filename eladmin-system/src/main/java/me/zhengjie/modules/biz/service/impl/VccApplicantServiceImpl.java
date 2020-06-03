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

import me.zhengjie.api.domain.biz.VccApplicant;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.api.repository.biz.VccApplicantRepository;
import me.zhengjie.modules.biz.service.VccApplicantService;
import me.zhengjie.modules.biz.service.dto.VccApplicantDto;
import me.zhengjie.modules.biz.service.dto.VccApplicantQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.VccApplicantMapper;
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
public class VccApplicantServiceImpl implements VccApplicantService {

    private final VccApplicantRepository VccApplicantRepository;
    private final VccApplicantMapper VccApplicantMapper;

    @Override
    public Map<String,Object> queryAll(VccApplicantQueryCriteria criteria, Pageable pageable){
        Page<VccApplicant> page = VccApplicantRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(VccApplicantMapper::toDto));
    }

    @Override
    public List<VccApplicantDto> queryAll(VccApplicantQueryCriteria criteria){
        return VccApplicantMapper.toDto(VccApplicantRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public VccApplicantDto findById(Long id) {
        VccApplicant VccApplicant = VccApplicantRepository.findById(id).orElseGet(VccApplicant::new);
        ValidationUtil.isNull(VccApplicant.getId(),"VccApplicant","id",id);
        return VccApplicantMapper.toDto(VccApplicant);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VccApplicantDto create(VccApplicant resources) {
        return VccApplicantMapper.toDto(VccApplicantRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(VccApplicant resources) {
        VccApplicant VccApplicant = VccApplicantRepository.findById(resources.getId()).orElseGet(VccApplicant::new);
        ValidationUtil.isNull( VccApplicant.getId(),"VccApplicant","id",resources.getId());
        BeanUtil.copyProperties(resources, VccApplicant, CopyOptions.create().setIgnoreNullValue(true));
        VccApplicantRepository.save(VccApplicant);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            VccApplicantRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<VccApplicantDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (VccApplicantDto VccApplicant : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" resumeId",  VccApplicant.getResumeId());
            map.put(" vacancyId",  VccApplicant.getVacancyId());
            map.put(" status",  VccApplicant.getStatus());
            map.put(" createdAt",  VccApplicant.getCreatedAt());
            map.put(" updatedAt",  VccApplicant.getUpdatedAt());
            map.put(" willInterviewAt",  VccApplicant.getWillInterviewAt());
            map.put(" interviewComment",  VccApplicant.getInterviewComment());
            map.put(" timeline",  VccApplicant.getTimeline());
            map.put("面试地点", VccApplicant.getInterviewPlace());
            map.put("联系人", VccApplicant.getInterviewPerson());
            map.put("面试电话", VccApplicant.getInterviewMobile());
            map.put("录用说明", VccApplicant.getHireComment());
            map.put("不合适说明", VccApplicant.getImproperComment());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}