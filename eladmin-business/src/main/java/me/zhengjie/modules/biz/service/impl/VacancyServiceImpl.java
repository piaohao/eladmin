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

import me.zhengjie.api.domain.biz.Vacancy;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.api.repository.biz.VacancyRepository;
import me.zhengjie.modules.biz.service.VacancyService;
import me.zhengjie.modules.biz.service.dto.VacancyDto;
import me.zhengjie.modules.biz.service.dto.VacancyQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.VacancyMapper;
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
public class VacancyServiceImpl implements VacancyService {

    private final VacancyRepository vacancyRepository;
    private final VacancyMapper vacancyMapper;

    @Override
    public Map<String,Object> queryAll(VacancyQueryCriteria criteria, Pageable pageable){
        Page<Vacancy> page = vacancyRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(vacancyMapper::toDto));
    }

    @Override
    public List<VacancyDto> queryAll(VacancyQueryCriteria criteria){
        return vacancyMapper.toDto(vacancyRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public VacancyDto findById(Long id) {
        Vacancy vacancy = vacancyRepository.findById(id).orElseGet(Vacancy::new);
        ValidationUtil.isNull(vacancy.getId(),"Vacancy","id",id);
        return vacancyMapper.toDto(vacancy);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VacancyDto create(Vacancy resources) {
        return vacancyMapper.toDto(vacancyRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Vacancy resources) {
        Vacancy vacancy = vacancyRepository.findById(resources.getId()).orElseGet(Vacancy::new);
        ValidationUtil.isNull( vacancy.getId(),"Vacancy","id",resources.getId());
        BeanUtil.copyProperties(resources, vacancy, CopyOptions.create().setIgnoreNullValue(true));
        vacancyRepository.save(vacancy);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            vacancyRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<VacancyDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (VacancyDto vacancy : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" companyId",  vacancy.getCompanyId());
            map.put(" name",  vacancy.getName());
            map.put(" address",  vacancy.getAddress());
            map.put(" description",  vacancy.getDescription());
            map.put(" status",  vacancy.getStatus());
            map.put(" createdAt",  vacancy.getCreatedAt());
            map.put(" updatedAt",  vacancy.getUpdatedAt());
            map.put(" isHottest",  vacancy.getIsHottest());
            map.put(" viewCount",  vacancy.getViewCount());
            map.put(" applyCount",  vacancy.getApplyCount());
            map.put(" stateId",  vacancy.getStateId());
            map.put(" cityId",  vacancy.getCityId());
            map.put(" districtId",  vacancy.getDistrictId());
            map.put(" lat",  vacancy.getLat());
            map.put(" lng",  vacancy.getLng());
            map.put(" vccTypeId",  vacancy.getVccTypeId());
            map.put(" educationId",  vacancy.getEducationId());
            map.put(" isIntern",  vacancy.getIsIntern());
            map.put(" internDuration",  vacancy.getInternDuration());
            map.put("日，月，年", vacancy.getSalaryType());
            map.put(" salaryRangeLow",  vacancy.getSalaryRangeLow());
            map.put(" salaryRangeHigh",  vacancy.getSalaryRangeHigh());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}