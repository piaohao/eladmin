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

import me.zhengjie.api.domain.biz.Mentor;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.api.repository.biz.MentorRepository;
import me.zhengjie.modules.biz.service.MentorService;
import me.zhengjie.modules.biz.service.dto.MentorDto;
import me.zhengjie.modules.biz.service.dto.MentorQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.MentorMapper;
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
public class MentorServiceImpl implements MentorService {

    private final MentorRepository mentorRepository;
    private final MentorMapper mentorMapper;

    @Override
    public Map<String,Object> queryAll(MentorQueryCriteria criteria, Pageable pageable){
        Page<Mentor> page = mentorRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(mentorMapper::toDto));
    }

    @Override
    public List<MentorDto> queryAll(MentorQueryCriteria criteria){
        return mentorMapper.toDto(mentorRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public MentorDto findById(Long id) {
        Mentor mentor = mentorRepository.findById(id).orElseGet(Mentor::new);
        ValidationUtil.isNull(mentor.getId(),"Mentor","id",id);
        return mentorMapper.toDto(mentor);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MentorDto create(Mentor resources) {
        return mentorMapper.toDto(mentorRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Mentor resources) {
        Mentor mentor = mentorRepository.findById(resources.getId()).orElseGet(Mentor::new);
        ValidationUtil.isNull( mentor.getId(),"Mentor","id",resources.getId());
        BeanUtil.copyProperties(resources, mentor, CopyOptions.create().setIgnoreNullValue(true));
        mentorRepository.save(mentor);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            mentorRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<MentorDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (MentorDto mentor : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" companyId",  mentor.getCompanyId());
            map.put(" name",  mentor.getName());
            map.put(" gender",  mentor.getGender());
            map.put(" age",  mentor.getAge());
            map.put(" photo",  mentor.getPhoto());
            map.put(" joinedAt",  mentor.getJoinedAt());
            map.put(" tags",  mentor.getTags());
            map.put(" contactMobile",  mentor.getContactMobile());
            map.put(" position",  mentor.getPosition());
            map.put(" profile",  mentor.getProfile());
            map.put(" createdAt",  mentor.getCreatedAt());
            map.put(" updatedAt",  mentor.getUpdatedAt());
            map.put("专业id", mentor.getMajorId());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}