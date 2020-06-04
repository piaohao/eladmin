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
public class VacancyBookmarkServiceImpl implements VacancyBookmarkService {

    private final VacancyBookmarkRepository vacancyBookmarkRepository;
    private final VacancyBookmarkMapper vacancyBookmarkMapper;

    @Override
    public Map<String,Object> queryAll(VacancyBookmarkQueryCriteria criteria, Pageable pageable){
        Page<VacancyBookmark> page = vacancyBookmarkRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(vacancyBookmarkMapper::toDto));
    }

    @Override
    public List<VacancyBookmarkDto> queryAll(VacancyBookmarkQueryCriteria criteria){
        return vacancyBookmarkMapper.toDto(vacancyBookmarkRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public VacancyBookmarkDto findById(Long id) {
        VacancyBookmark vacancyBookmark = vacancyBookmarkRepository.findById(id).orElseGet(VacancyBookmark::new);
        ValidationUtil.isNull(vacancyBookmark.getId(),"VacancyBookmark","id",id);
        return vacancyBookmarkMapper.toDto(vacancyBookmark);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VacancyBookmarkDto create(VacancyBookmark resources) {
        return vacancyBookmarkMapper.toDto(vacancyBookmarkRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(VacancyBookmark resources) {
        VacancyBookmark vacancyBookmark = vacancyBookmarkRepository.findById(resources.getId()).orElseGet(VacancyBookmark::new);
        ValidationUtil.isNull( vacancyBookmark.getId(),"VacancyBookmark","id",resources.getId());
        BeanUtil.copyProperties(resources, vacancyBookmark, CopyOptions.create().setIgnoreNullValue(true));
        vacancyBookmarkRepository.save(vacancyBookmark);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            vacancyBookmarkRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<VacancyBookmarkDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (VacancyBookmarkDto vacancyBookmark : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" vacancyId",  vacancyBookmark.getVacancyId());
            map.put(" studentUserId",  vacancyBookmark.getStudentUserId());
            map.put(" createdAt",  vacancyBookmark.getCreatedAt());
            map.put(" updatedAt",  vacancyBookmark.getUpdatedAt());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}