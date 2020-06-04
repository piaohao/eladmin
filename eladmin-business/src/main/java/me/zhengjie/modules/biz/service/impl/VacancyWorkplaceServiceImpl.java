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

import me.zhengjie.api.domain.biz.VacancyWorkplace;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.api.repository.biz.VacancyWorkplaceRepository;
import me.zhengjie.modules.biz.service.VacancyWorkplaceService;
import me.zhengjie.modules.biz.service.dto.VacancyWorkplaceDto;
import me.zhengjie.modules.biz.service.dto.VacancyWorkplaceQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.VacancyWorkplaceMapper;
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
public class VacancyWorkplaceServiceImpl implements VacancyWorkplaceService {

    private final VacancyWorkplaceRepository vacancyWorkplaceRepository;
    private final VacancyWorkplaceMapper vacancyWorkplaceMapper;

    @Override
    public Map<String,Object> queryAll(VacancyWorkplaceQueryCriteria criteria, Pageable pageable){
        Page<VacancyWorkplace> page = vacancyWorkplaceRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(vacancyWorkplaceMapper::toDto));
    }

    @Override
    public List<VacancyWorkplaceDto> queryAll(VacancyWorkplaceQueryCriteria criteria){
        return vacancyWorkplaceMapper.toDto(vacancyWorkplaceRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public VacancyWorkplaceDto findById(Long id) {
        VacancyWorkplace vacancyWorkplace = vacancyWorkplaceRepository.findById(id).orElseGet(VacancyWorkplace::new);
        ValidationUtil.isNull(vacancyWorkplace.getId(),"VacancyWorkplace","id",id);
        return vacancyWorkplaceMapper.toDto(vacancyWorkplace);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VacancyWorkplaceDto create(VacancyWorkplace resources) {
        return vacancyWorkplaceMapper.toDto(vacancyWorkplaceRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(VacancyWorkplace resources) {
        VacancyWorkplace vacancyWorkplace = vacancyWorkplaceRepository.findById(resources.getId()).orElseGet(VacancyWorkplace::new);
        ValidationUtil.isNull( vacancyWorkplace.getId(),"VacancyWorkplace","id",resources.getId());
        BeanUtil.copyProperties(resources, vacancyWorkplace, CopyOptions.create().setIgnoreNullValue(true));
        vacancyWorkplaceRepository.save(vacancyWorkplace);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            vacancyWorkplaceRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<VacancyWorkplaceDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (VacancyWorkplaceDto vacancyWorkplace : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("职位id", vacancyWorkplace.getVacancyId());
            map.put("城市id", vacancyWorkplace.getCityId());
            map.put("地址", vacancyWorkplace.getAddress());
            map.put("街道", vacancyWorkplace.getStreet());
            map.put("经度", vacancyWorkplace.getLongitude());
            map.put("纬度", vacancyWorkplace.getLatitude());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}