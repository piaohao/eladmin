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

import me.zhengjie.api.domain.biz.VccType;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.api.repository.biz.VccTypeRepository;
import me.zhengjie.modules.biz.service.VccTypeService;
import me.zhengjie.modules.biz.service.dto.VccTypeDto;
import me.zhengjie.modules.biz.service.dto.VccTypeQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.VccTypeMapper;
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
public class VccTypeServiceImpl implements VccTypeService {

    private final VccTypeRepository VccTypeRepository;
    private final VccTypeMapper VccTypeMapper;

    @Override
    public Map<String,Object> queryAll(VccTypeQueryCriteria criteria, Pageable pageable){
        Page<VccType> page = VccTypeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(VccTypeMapper::toDto));
    }

    @Override
    public List<VccTypeDto> queryAll(VccTypeQueryCriteria criteria){
        return VccTypeMapper.toDto(VccTypeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public VccTypeDto findById(Long id) {
        VccType VccType = VccTypeRepository.findById(id).orElseGet(VccType::new);
        ValidationUtil.isNull(VccType.getId(),"VccType","id",id);
        return VccTypeMapper.toDto(VccType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VccTypeDto create(VccType resources) {
        return VccTypeMapper.toDto(VccTypeRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(VccType resources) {
        VccType VccType = VccTypeRepository.findById(resources.getId()).orElseGet(VccType::new);
        ValidationUtil.isNull( VccType.getId(),"VccType","id",resources.getId());
        BeanUtil.copyProperties(resources, VccType, CopyOptions.create().setIgnoreNullValue(true));
        VccTypeRepository.save(VccType);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            VccTypeRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<VccTypeDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (VccTypeDto VccType : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" name",  VccType.getName());
            map.put(" parentId",  VccType.getParentId());
            map.put(" ancestry",  VccType.getAncestry());
            map.put(" createdAt",  VccType.getCreatedAt());
            map.put(" updatedAt",  VccType.getUpdatedAt());
            map.put(" isHottest",  VccType.getIsHottest());
            map.put(" icon",  VccType.getIcon());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}