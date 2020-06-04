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
public class ResumeServiceImpl implements ResumeService {

    private final ResumeRepository resumeRepository;
    private final ResumeMapper resumeMapper;

    @Override
    public Map<String,Object> queryAll(ResumeQueryCriteria criteria, Pageable pageable){
        Page<Resume> page = resumeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(resumeMapper::toDto));
    }

    @Override
    public List<ResumeDto> queryAll(ResumeQueryCriteria criteria){
        return resumeMapper.toDto(resumeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public ResumeDto findById(Long id) {
        Resume resume = resumeRepository.findById(id).orElseGet(Resume::new);
        ValidationUtil.isNull(resume.getId(),"Resume","id",id);
        return resumeMapper.toDto(resume);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResumeDto create(Resume resources) {
        return resumeMapper.toDto(resumeRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Resume resources) {
        Resume resume = resumeRepository.findById(resources.getId()).orElseGet(Resume::new);
        ValidationUtil.isNull( resume.getId(),"Resume","id",resources.getId());
        BeanUtil.copyProperties(resources, resume, CopyOptions.create().setIgnoreNullValue(true));
        resumeRepository.save(resume);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            resumeRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<ResumeDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ResumeDto resume : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" studentUserId",  resume.getStudentUserId());
            map.put(" name",  resume.getName());
            map.put(" gender",  resume.getGender());
            map.put(" dateOfBirth",  resume.getDateOfBirth());
            map.put(" mobile",  resume.getMobile());
            map.put(" createdAt",  resume.getCreatedAt());
            map.put(" updatedAt",  resume.getUpdatedAt());
            map.put("城市id", resume.getCityId());
            map.put("邮箱", resume.getEmail());
            map.put("头像", resume.getAvatar());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}