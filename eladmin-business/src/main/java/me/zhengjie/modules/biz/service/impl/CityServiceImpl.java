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
import me.zhengjie.api.domain.biz.City;
import me.zhengjie.api.repository.biz.CityRepository;
import me.zhengjie.modules.biz.service.CityService;
import me.zhengjie.modules.biz.service.dto.CityDto;
import me.zhengjie.modules.biz.service.dto.CityQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.CityMapper;
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
public class CityServiceImpl implements CityService {

    private final CityRepository CityRepository;
    private final CityMapper CityMapper;

    @Override
    public Map<String,Object> queryAll(CityQueryCriteria criteria, Pageable pageable){
        Page<City> page = CityRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(CityMapper::toDto));
    }

    @Override
    public List<CityDto> queryAll(CityQueryCriteria criteria){
        return CityMapper.toDto(CityRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public CityDto findById(Long id) {
        City City = CityRepository.findById(id).orElseGet(City::new);
        ValidationUtil.isNull(City.getId(),"City","id",id);
        return CityMapper.toDto(City);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CityDto create(City resources) {
        return CityMapper.toDto(CityRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(City resources) {
        City City = CityRepository.findById(resources.getId()).orElseGet(City::new);
        ValidationUtil.isNull( City.getId(),"City","id",resources.getId());
        BeanUtil.copyProperties(resources, City, CopyOptions.create().setIgnoreNullValue(true));
        CityRepository.save(City);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            CityRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<CityDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (CityDto City : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" stateId",  City.getStateId());
            map.put(" name",  City.getName());
            map.put(" createdAt",  City.getCreatedAt());
            map.put(" updatedAt",  City.getUpdatedAt());
            map.put(" firstCap",  City.getFirstCap());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}