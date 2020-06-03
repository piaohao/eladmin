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
import me.zhengjie.api.domain.biz.District;
import me.zhengjie.api.repository.biz.DistrictRepository;
import me.zhengjie.modules.biz.service.DistrictService;
import me.zhengjie.modules.biz.service.dto.DistrictDto;
import me.zhengjie.modules.biz.service.dto.DistrictQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.DistrictMapper;
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
public class DistrictServiceImpl implements DistrictService {

    private final DistrictRepository DistrictRepository;
    private final DistrictMapper DistrictMapper;

    @Override
    public Map<String,Object> queryAll(DistrictQueryCriteria criteria, Pageable pageable){
        Page<District> page = DistrictRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(DistrictMapper::toDto));
    }

    @Override
    public List<DistrictDto> queryAll(DistrictQueryCriteria criteria){
        return DistrictMapper.toDto(DistrictRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public DistrictDto findById(Long id) {
        District District = DistrictRepository.findById(id).orElseGet(District::new);
        ValidationUtil.isNull(District.getId(),"District","id",id);
        return DistrictMapper.toDto(District);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DistrictDto create(District resources) {
        return DistrictMapper.toDto(DistrictRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(District resources) {
        District District = DistrictRepository.findById(resources.getId()).orElseGet(District::new);
        ValidationUtil.isNull( District.getId(),"District","id",resources.getId());
        BeanUtil.copyProperties(resources, District, CopyOptions.create().setIgnoreNullValue(true));
        DistrictRepository.save(District);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            DistrictRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<DistrictDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (DistrictDto District : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" cityId",  District.getCityId());
            map.put(" name",  District.getName());
            map.put(" createdAt",  District.getCreatedAt());
            map.put(" updatedAt",  District.getUpdatedAt());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}