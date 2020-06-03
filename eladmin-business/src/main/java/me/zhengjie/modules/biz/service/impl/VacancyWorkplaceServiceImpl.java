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

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.RequiredArgsConstructor;
import me.zhengjie.api.domain.biz.VacancyWorkplace;
import me.zhengjie.api.repository.biz.VacancyWorkplaceRepository;
import me.zhengjie.modules.biz.service.VacancyWorkplaceService;
import me.zhengjie.modules.biz.service.dto.VacancyWorkplaceDto;
import me.zhengjie.modules.biz.service.dto.VacancyWorkplaceQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.VacancyWorkplaceMapper;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import me.zhengjie.utils.ValidationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
* @website https://el-admin.vip
* @description 服务实现
* @author piaohao
* @date 2020-06-03
**/
@Service
@RequiredArgsConstructor
public class VacancyWorkplaceServiceImpl implements VacancyWorkplaceService {

    private final VacancyWorkplaceRepository VacancyWorkplaceRepository;
    private final VacancyWorkplaceMapper VacancyWorkplaceMapper;

    @Override
    public Map<String,Object> queryAll(VacancyWorkplaceQueryCriteria criteria, Pageable pageable){
        Page<VacancyWorkplace> page = VacancyWorkplaceRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(VacancyWorkplaceMapper::toDto));
    }

    @Override
    public List<VacancyWorkplaceDto> queryAll(VacancyWorkplaceQueryCriteria criteria){
        return VacancyWorkplaceMapper.toDto(VacancyWorkplaceRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public VacancyWorkplaceDto findById(Long id) {
        VacancyWorkplace VacancyWorkplace = VacancyWorkplaceRepository.findById(id).orElseGet(VacancyWorkplace::new);
        ValidationUtil.isNull(VacancyWorkplace.getId(),"VacancyWorkplace","id",id);
        return VacancyWorkplaceMapper.toDto(VacancyWorkplace);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VacancyWorkplaceDto create(VacancyWorkplace resources) {
        return VacancyWorkplaceMapper.toDto(VacancyWorkplaceRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(VacancyWorkplace resources) {
        VacancyWorkplace VacancyWorkplace = VacancyWorkplaceRepository.findById(resources.getId()).orElseGet(VacancyWorkplace::new);
        ValidationUtil.isNull( VacancyWorkplace.getId(),"VacancyWorkplace","id",resources.getId());
        BeanUtil.copyProperties(resources, VacancyWorkplace, CopyOptions.create().setIgnoreNullValue(true));
        VacancyWorkplaceRepository.save(VacancyWorkplace);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            VacancyWorkplaceRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<VacancyWorkplaceDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (VacancyWorkplaceDto VacancyWorkplace : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("职位id", VacancyWorkplace.getVacancyId());
            map.put("城市id", VacancyWorkplace.getCityId());
            map.put("地址", VacancyWorkplace.getAddress());
            map.put("街道", VacancyWorkplace.getStreet());
            map.put("经度", VacancyWorkplace.getLongitude());
            map.put("纬度", VacancyWorkplace.getLatitude());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}