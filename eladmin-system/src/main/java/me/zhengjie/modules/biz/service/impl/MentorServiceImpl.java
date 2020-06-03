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
public class MentorServiceImpl implements MentorService {

    private final MentorRepository MentorRepository;
    private final MentorMapper MentorMapper;

    @Override
    public Map<String,Object> queryAll(MentorQueryCriteria criteria, Pageable pageable){
        Page<Mentor> page = MentorRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(MentorMapper::toDto));
    }

    @Override
    public List<MentorDto> queryAll(MentorQueryCriteria criteria){
        return MentorMapper.toDto(MentorRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public MentorDto findById(Long id) {
        Mentor Mentor = MentorRepository.findById(id).orElseGet(Mentor::new);
        ValidationUtil.isNull(Mentor.getId(),"Mentor","id",id);
        return MentorMapper.toDto(Mentor);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MentorDto create(Mentor resources) {
        return MentorMapper.toDto(MentorRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Mentor resources) {
        Mentor Mentor = MentorRepository.findById(resources.getId()).orElseGet(Mentor::new);
        ValidationUtil.isNull( Mentor.getId(),"Mentor","id",resources.getId());
        BeanUtil.copyProperties(resources, Mentor, CopyOptions.create().setIgnoreNullValue(true));
        MentorRepository.save(Mentor);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            MentorRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<MentorDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (MentorDto Mentor : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" companyId",  Mentor.getCompanyId());
            map.put(" name",  Mentor.getName());
            map.put(" gender",  Mentor.getGender());
            map.put(" age",  Mentor.getAge());
            map.put(" photo",  Mentor.getPhoto());
            map.put(" joinedAt",  Mentor.getJoinedAt());
            map.put(" tags",  Mentor.getTags());
            map.put(" contactMobile",  Mentor.getContactMobile());
            map.put(" position",  Mentor.getPosition());
            map.put(" profile",  Mentor.getProfile());
            map.put(" createdAt",  Mentor.getCreatedAt());
            map.put(" updatedAt",  Mentor.getUpdatedAt());
            map.put("专业id", Mentor.getMajorId());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}