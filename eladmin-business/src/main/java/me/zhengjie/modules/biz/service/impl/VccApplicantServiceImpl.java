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
public class VccApplicantServiceImpl implements VccApplicantService {

    private final VccApplicantRepository vccApplicantRepository;
    private final VccApplicantMapper vccApplicantMapper;

    @Override
    public Map<String,Object> queryAll(VccApplicantQueryCriteria criteria, Pageable pageable){
        Page<VccApplicant> page = vccApplicantRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(vccApplicantMapper::toDto));
    }

    @Override
    public List<VccApplicantDto> queryAll(VccApplicantQueryCriteria criteria){
        return vccApplicantMapper.toDto(vccApplicantRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public VccApplicantDto findById(Long id) {
        VccApplicant vccApplicant = vccApplicantRepository.findById(id).orElseGet(VccApplicant::new);
        ValidationUtil.isNull(vccApplicant.getId(),"VccApplicant","id",id);
        return vccApplicantMapper.toDto(vccApplicant);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VccApplicantDto create(VccApplicant resources) {
        return vccApplicantMapper.toDto(vccApplicantRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(VccApplicant resources) {
        VccApplicant vccApplicant = vccApplicantRepository.findById(resources.getId()).orElseGet(VccApplicant::new);
        ValidationUtil.isNull( vccApplicant.getId(),"VccApplicant","id",resources.getId());
        BeanUtil.copyProperties(resources, vccApplicant, CopyOptions.create().setIgnoreNullValue(true));
        vccApplicantRepository.save(vccApplicant);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            vccApplicantRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<VccApplicantDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (VccApplicantDto vccApplicant : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" resumeId",  vccApplicant.getResumeId());
            map.put(" vacancyId",  vccApplicant.getVacancyId());
            map.put(" status",  vccApplicant.getStatus());
            map.put(" createdAt",  vccApplicant.getCreatedAt());
            map.put(" updatedAt",  vccApplicant.getUpdatedAt());
            map.put(" willInterviewAt",  vccApplicant.getWillInterviewAt());
            map.put(" interviewComment",  vccApplicant.getInterviewComment());
            map.put(" timeline",  vccApplicant.getTimeline());
            map.put("面试地点", vccApplicant.getInterviewPlace());
            map.put("联系人", vccApplicant.getInterviewPerson());
            map.put("面试电话", vccApplicant.getInterviewMobile());
            map.put("录用说明", vccApplicant.getHireComment());
            map.put("不合适说明", vccApplicant.getImproperComment());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}