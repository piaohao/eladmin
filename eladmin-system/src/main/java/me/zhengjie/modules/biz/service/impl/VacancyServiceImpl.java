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
public class VacancyServiceImpl implements VacancyService {

    private final VacancyRepository VacancyRepository;
    private final VacancyMapper VacancyMapper;

    @Override
    public Map<String,Object> queryAll(VacancyQueryCriteria criteria, Pageable pageable){
        Page<Vacancy> page = VacancyRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(VacancyMapper::toDto));
    }

    @Override
    public List<VacancyDto> queryAll(VacancyQueryCriteria criteria){
        return VacancyMapper.toDto(VacancyRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public VacancyDto findById(Long id) {
        Vacancy Vacancy = VacancyRepository.findById(id).orElseGet(Vacancy::new);
        ValidationUtil.isNull(Vacancy.getId(),"Vacancy","id",id);
        return VacancyMapper.toDto(Vacancy);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VacancyDto create(Vacancy resources) {
        return VacancyMapper.toDto(VacancyRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Vacancy resources) {
        Vacancy Vacancy = VacancyRepository.findById(resources.getId()).orElseGet(Vacancy::new);
        ValidationUtil.isNull( Vacancy.getId(),"Vacancy","id",resources.getId());
        BeanUtil.copyProperties(resources, Vacancy, CopyOptions.create().setIgnoreNullValue(true));
        VacancyRepository.save(Vacancy);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            VacancyRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<VacancyDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (VacancyDto Vacancy : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" companyId",  Vacancy.getCompanyId());
            map.put(" name",  Vacancy.getName());
            map.put(" address",  Vacancy.getAddress());
            map.put(" description",  Vacancy.getDescription());
            map.put(" status",  Vacancy.getStatus());
            map.put(" createdAt",  Vacancy.getCreatedAt());
            map.put(" updatedAt",  Vacancy.getUpdatedAt());
            map.put(" isHottest",  Vacancy.getIsHottest());
            map.put(" viewCount",  Vacancy.getViewCount());
            map.put(" applyCount",  Vacancy.getApplyCount());
            map.put(" stateId",  Vacancy.getStateId());
            map.put(" cityId",  Vacancy.getCityId());
            map.put(" districtId",  Vacancy.getDistrictId());
            map.put(" lat",  Vacancy.getLat());
            map.put(" lng",  Vacancy.getLng());
            map.put(" vccTypeId",  Vacancy.getVccTypeId());
            map.put(" educationId",  Vacancy.getEducationId());
            map.put(" isIntern",  Vacancy.getIsIntern());
            map.put(" internDuration",  Vacancy.getInternDuration());
            map.put("日，月，年", Vacancy.getSalaryType());
            map.put(" salaryRangeLow",  Vacancy.getSalaryRangeLow());
            map.put(" salaryRangeHigh",  Vacancy.getSalaryRangeHigh());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}