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

import me.zhengjie.api.domain.biz.VacancyBookmark;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.api.repository.biz.VacancyBookmarkRepository;
import me.zhengjie.modules.biz.service.VacancyBookmarkService;
import me.zhengjie.modules.biz.service.dto.VacancyBookmarkDto;
import me.zhengjie.modules.biz.service.dto.VacancyBookmarkQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.VacancyBookmarkMapper;
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
public class VacancyBookmarkServiceImpl implements VacancyBookmarkService {

    private final VacancyBookmarkRepository VacancyBookmarkRepository;
    private final VacancyBookmarkMapper VacancyBookmarkMapper;

    @Override
    public Map<String,Object> queryAll(VacancyBookmarkQueryCriteria criteria, Pageable pageable){
        Page<VacancyBookmark> page = VacancyBookmarkRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(VacancyBookmarkMapper::toDto));
    }

    @Override
    public List<VacancyBookmarkDto> queryAll(VacancyBookmarkQueryCriteria criteria){
        return VacancyBookmarkMapper.toDto(VacancyBookmarkRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public VacancyBookmarkDto findById(Long id) {
        VacancyBookmark VacancyBookmark = VacancyBookmarkRepository.findById(id).orElseGet(VacancyBookmark::new);
        ValidationUtil.isNull(VacancyBookmark.getId(),"VacancyBookmark","id",id);
        return VacancyBookmarkMapper.toDto(VacancyBookmark);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VacancyBookmarkDto create(VacancyBookmark resources) {
        return VacancyBookmarkMapper.toDto(VacancyBookmarkRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(VacancyBookmark resources) {
        VacancyBookmark VacancyBookmark = VacancyBookmarkRepository.findById(resources.getId()).orElseGet(VacancyBookmark::new);
        ValidationUtil.isNull( VacancyBookmark.getId(),"VacancyBookmark","id",resources.getId());
        BeanUtil.copyProperties(resources, VacancyBookmark, CopyOptions.create().setIgnoreNullValue(true));
        VacancyBookmarkRepository.save(VacancyBookmark);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            VacancyBookmarkRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<VacancyBookmarkDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (VacancyBookmarkDto VacancyBookmark : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" vacancyId",  VacancyBookmark.getVacancyId());
            map.put(" studentUserId",  VacancyBookmark.getStudentUserId());
            map.put(" createdAt",  VacancyBookmark.getCreatedAt());
            map.put(" updatedAt",  VacancyBookmark.getUpdatedAt());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}