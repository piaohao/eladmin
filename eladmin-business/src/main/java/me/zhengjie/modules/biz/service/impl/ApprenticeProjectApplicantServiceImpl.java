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

import me.zhengjie.api.domain.biz.ApprenticeProjectApplicant;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.api.repository.biz.ApprenticeProjectApplicantRepository;
import me.zhengjie.modules.biz.service.ApprenticeProjectApplicantService;
import me.zhengjie.modules.biz.service.dto.ApprenticeProjectApplicantDto;
import me.zhengjie.modules.biz.service.dto.ApprenticeProjectApplicantQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.ApprenticeProjectApplicantMapper;
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
public class ApprenticeProjectApplicantServiceImpl implements ApprenticeProjectApplicantService {

    private final ApprenticeProjectApplicantRepository apprenticeProjectApplicantRepository;
    private final ApprenticeProjectApplicantMapper apprenticeProjectApplicantMapper;

    @Override
    public Map<String,Object> queryAll(ApprenticeProjectApplicantQueryCriteria criteria, Pageable pageable){
        Page<ApprenticeProjectApplicant> page = apprenticeProjectApplicantRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(apprenticeProjectApplicantMapper::toDto));
    }

    @Override
    public List<ApprenticeProjectApplicantDto> queryAll(ApprenticeProjectApplicantQueryCriteria criteria){
        return apprenticeProjectApplicantMapper.toDto(apprenticeProjectApplicantRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public ApprenticeProjectApplicantDto findById(Long id) {
        ApprenticeProjectApplicant apprenticeProjectApplicant = apprenticeProjectApplicantRepository.findById(id).orElseGet(ApprenticeProjectApplicant::new);
        ValidationUtil.isNull(apprenticeProjectApplicant.getId(),"ApprenticeProjectApplicant","id",id);
        return apprenticeProjectApplicantMapper.toDto(apprenticeProjectApplicant);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApprenticeProjectApplicantDto create(ApprenticeProjectApplicant resources) {
        return apprenticeProjectApplicantMapper.toDto(apprenticeProjectApplicantRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ApprenticeProjectApplicant resources) {
        ApprenticeProjectApplicant apprenticeProjectApplicant = apprenticeProjectApplicantRepository.findById(resources.getId()).orElseGet(ApprenticeProjectApplicant::new);
        ValidationUtil.isNull( apprenticeProjectApplicant.getId(),"ApprenticeProjectApplicant","id",resources.getId());
        BeanUtil.copyProperties(resources, apprenticeProjectApplicant, CopyOptions.create().setIgnoreNullValue(true));
        apprenticeProjectApplicantRepository.save(apprenticeProjectApplicant);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            apprenticeProjectApplicantRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<ApprenticeProjectApplicantDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ApprenticeProjectApplicantDto apprenticeProjectApplicant : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" apprenticeProjectId",  apprenticeProjectApplicant.getApprenticeProjectId());
            map.put(" studentUserId",  apprenticeProjectApplicant.getStudentUserId());
            map.put(" status",  apprenticeProjectApplicant.getStatus());
            map.put(" createdAt",  apprenticeProjectApplicant.getCreatedAt());
            map.put(" updatedAt",  apprenticeProjectApplicant.getUpdatedAt());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}