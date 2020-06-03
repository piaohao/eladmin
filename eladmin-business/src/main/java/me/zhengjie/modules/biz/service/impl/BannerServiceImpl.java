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
import me.zhengjie.api.domain.biz.Banner;
import me.zhengjie.api.repository.biz.BannerRepository;
import me.zhengjie.modules.biz.service.BannerService;
import me.zhengjie.modules.biz.service.dto.BannerDto;
import me.zhengjie.modules.biz.service.dto.BannerQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.BannerMapper;
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
public class BannerServiceImpl implements BannerService {

    private final BannerRepository BannerRepository;
    private final BannerMapper BannerMapper;

    @Override
    public Map<String,Object> queryAll(BannerQueryCriteria criteria, Pageable pageable){
        Page<Banner> page = BannerRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(BannerMapper::toDto));
    }

    @Override
    public List<BannerDto> queryAll(BannerQueryCriteria criteria){
        return BannerMapper.toDto(BannerRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public BannerDto findById(Long id) {
        Banner Banner = BannerRepository.findById(id).orElseGet(Banner::new);
        ValidationUtil.isNull(Banner.getId(),"Banner","id",id);
        return BannerMapper.toDto(Banner);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BannerDto create(Banner resources) {
        return BannerMapper.toDto(BannerRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Banner resources) {
        Banner Banner = BannerRepository.findById(resources.getId()).orElseGet(Banner::new);
        ValidationUtil.isNull( Banner.getId(),"Banner","id",resources.getId());
        BeanUtil.copyProperties(resources, Banner, CopyOptions.create().setIgnoreNullValue(true));
        BannerRepository.save(Banner);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            BannerRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<BannerDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (BannerDto Banner : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" imageUrl",  Banner.getImageUrl());
            map.put(" displayIndex",  Banner.getDisplayIndex());
            map.put(" actionUrl",  Banner.getActionUrl());
            map.put(" createdAt",  Banner.getCreatedAt());
            map.put(" updatedAt",  Banner.getUpdatedAt());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}