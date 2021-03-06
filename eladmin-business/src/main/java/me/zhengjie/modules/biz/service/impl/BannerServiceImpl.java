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

import me.zhengjie.api.domain.biz.Banner;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.api.repository.biz.BannerRepository;
import me.zhengjie.modules.biz.service.BannerService;
import me.zhengjie.modules.biz.service.dto.BannerDto;
import me.zhengjie.modules.biz.service.dto.BannerQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.BannerMapper;
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
public class BannerServiceImpl implements BannerService {

    private final BannerRepository bannerRepository;
    private final BannerMapper bannerMapper;

    @Override
    public Map<String,Object> queryAll(BannerQueryCriteria criteria, Pageable pageable){
        Page<Banner> page = bannerRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(bannerMapper::toDto));
    }

    @Override
    public List<BannerDto> queryAll(BannerQueryCriteria criteria){
        return bannerMapper.toDto(bannerRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public BannerDto findById(Long id) {
        Banner banner = bannerRepository.findById(id).orElseGet(Banner::new);
        ValidationUtil.isNull(banner.getId(),"Banner","id",id);
        return bannerMapper.toDto(banner);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BannerDto create(Banner resources) {
        return bannerMapper.toDto(bannerRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Banner resources) {
        Banner banner = bannerRepository.findById(resources.getId()).orElseGet(Banner::new);
        ValidationUtil.isNull( banner.getId(),"Banner","id",resources.getId());
        BeanUtil.copyProperties(resources, banner, CopyOptions.create().setIgnoreNullValue(true));
        bannerRepository.save(banner);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            bannerRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<BannerDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (BannerDto banner : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" imageUrl",  banner.getImageUrl());
            map.put(" displayIndex",  banner.getDisplayIndex());
            map.put(" actionUrl",  banner.getActionUrl());
            map.put(" createdAt",  banner.getCreatedAt());
            map.put(" updatedAt",  banner.getUpdatedAt());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}