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
public class VccTypeServiceImpl implements VccTypeService {

    private final VccTypeRepository vccTypeRepository;
    private final VccTypeMapper vccTypeMapper;

    @Override
    public Map<String,Object> queryAll(VccTypeQueryCriteria criteria, Pageable pageable){
        Page<VccType> page = vccTypeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(vccTypeMapper::toDto));
    }

    @Override
    public List<VccTypeDto> queryAll(VccTypeQueryCriteria criteria){
        return vccTypeMapper.toDto(vccTypeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public VccTypeDto findById(Long id) {
        VccType vccType = vccTypeRepository.findById(id).orElseGet(VccType::new);
        ValidationUtil.isNull(vccType.getId(),"VccType","id",id);
        return vccTypeMapper.toDto(vccType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VccTypeDto create(VccType resources) {
        return vccTypeMapper.toDto(vccTypeRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(VccType resources) {
        VccType vccType = vccTypeRepository.findById(resources.getId()).orElseGet(VccType::new);
        ValidationUtil.isNull( vccType.getId(),"VccType","id",resources.getId());
        BeanUtil.copyProperties(resources, vccType, CopyOptions.create().setIgnoreNullValue(true));
        vccTypeRepository.save(vccType);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            vccTypeRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<VccTypeDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (VccTypeDto vccType : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" name",  vccType.getName());
            map.put(" parentId",  vccType.getParentId());
            map.put(" ancestry",  vccType.getAncestry());
            map.put(" createdAt",  vccType.getCreatedAt());
            map.put(" updatedAt",  vccType.getUpdatedAt());
            map.put(" isHottest",  vccType.getIsHottest());
            map.put(" icon",  vccType.getIcon());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}