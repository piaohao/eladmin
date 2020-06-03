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

import me.zhengjie.api.domain.biz.Resume;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.api.repository.biz.ResumeRepository;
import me.zhengjie.modules.biz.service.ResumeService;
import me.zhengjie.modules.biz.service.dto.ResumeDto;
import me.zhengjie.modules.biz.service.dto.ResumeQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.ResumeMapper;
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
public class ResumeServiceImpl implements ResumeService {

    private final ResumeRepository ResumeRepository;
    private final ResumeMapper ResumeMapper;

    @Override
    public Map<String,Object> queryAll(ResumeQueryCriteria criteria, Pageable pageable){
        Page<Resume> page = ResumeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(ResumeMapper::toDto));
    }

    @Override
    public List<ResumeDto> queryAll(ResumeQueryCriteria criteria){
        return ResumeMapper.toDto(ResumeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public ResumeDto findById(Long id) {
        Resume Resume = ResumeRepository.findById(id).orElseGet(Resume::new);
        ValidationUtil.isNull(Resume.getId(),"Resume","id",id);
        return ResumeMapper.toDto(Resume);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResumeDto create(Resume resources) {
        return ResumeMapper.toDto(ResumeRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Resume resources) {
        Resume Resume = ResumeRepository.findById(resources.getId()).orElseGet(Resume::new);
        ValidationUtil.isNull( Resume.getId(),"Resume","id",resources.getId());
        BeanUtil.copyProperties(resources, Resume, CopyOptions.create().setIgnoreNullValue(true));
        ResumeRepository.save(Resume);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            ResumeRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<ResumeDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ResumeDto Resume : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" studentUserId",  Resume.getStudentUserId());
            map.put(" name",  Resume.getName());
            map.put(" gender",  Resume.getGender());
            map.put(" dateOfBirth",  Resume.getDateOfBirth());
            map.put(" mobile",  Resume.getMobile());
            map.put(" createdAt",  Resume.getCreatedAt());
            map.put(" updatedAt",  Resume.getUpdatedAt());
            map.put("城市id", Resume.getCityId());
            map.put("邮箱", Resume.getEmail());
            map.put("头像", Resume.getAvatar());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}