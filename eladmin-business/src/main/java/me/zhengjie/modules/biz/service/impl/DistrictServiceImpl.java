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

import me.zhengjie.api.domain.biz.District;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.api.repository.biz.DistrictRepository;
import me.zhengjie.modules.biz.service.DistrictService;
import me.zhengjie.modules.biz.service.dto.DistrictDto;
import me.zhengjie.modules.biz.service.dto.DistrictQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.DistrictMapper;
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
public class DistrictServiceImpl implements DistrictService {

    private final DistrictRepository districtRepository;
    private final DistrictMapper districtMapper;

    @Override
    public Map<String,Object> queryAll(DistrictQueryCriteria criteria, Pageable pageable){
        Page<District> page = districtRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(districtMapper::toDto));
    }

    @Override
    public List<DistrictDto> queryAll(DistrictQueryCriteria criteria){
        return districtMapper.toDto(districtRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public DistrictDto findById(Long id) {
        District district = districtRepository.findById(id).orElseGet(District::new);
        ValidationUtil.isNull(district.getId(),"District","id",id);
        return districtMapper.toDto(district);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DistrictDto create(District resources) {
        return districtMapper.toDto(districtRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(District resources) {
        District district = districtRepository.findById(resources.getId()).orElseGet(District::new);
        ValidationUtil.isNull( district.getId(),"District","id",resources.getId());
        BeanUtil.copyProperties(resources, district, CopyOptions.create().setIgnoreNullValue(true));
        districtRepository.save(district);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            districtRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<DistrictDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (DistrictDto district : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" cityId",  district.getCityId());
            map.put(" name",  district.getName());
            map.put(" createdAt",  district.getCreatedAt());
            map.put(" updatedAt",  district.getUpdatedAt());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}