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
import me.zhengjie.api.domain.biz.ResumeStatus;
import me.zhengjie.api.repository.biz.ResumeStatusRepository;
import me.zhengjie.modules.biz.service.ResumeStatusService;
import me.zhengjie.modules.biz.service.dto.ResumeStatusDto;
import me.zhengjie.modules.biz.service.dto.ResumeStatusQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.ResumeStatusMapper;
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
public class ResumeStatusServiceImpl implements ResumeStatusService {

    private final ResumeStatusRepository ResumeStatusRepository;
    private final ResumeStatusMapper ResumeStatusMapper;

    @Override
    public Map<String,Object> queryAll(ResumeStatusQueryCriteria criteria, Pageable pageable){
        Page<ResumeStatus> page = ResumeStatusRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(ResumeStatusMapper::toDto));
    }

    @Override
    public List<ResumeStatusDto> queryAll(ResumeStatusQueryCriteria criteria){
        return ResumeStatusMapper.toDto(ResumeStatusRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public ResumeStatusDto findById(Long id) {
        ResumeStatus ResumeStatus = ResumeStatusRepository.findById(id).orElseGet(ResumeStatus::new);
        ValidationUtil.isNull(ResumeStatus.getId(),"ResumeStatus","id",id);
        return ResumeStatusMapper.toDto(ResumeStatus);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResumeStatusDto create(ResumeStatus resources) {
        return ResumeStatusMapper.toDto(ResumeStatusRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ResumeStatus resources) {
        ResumeStatus ResumeStatus = ResumeStatusRepository.findById(resources.getId()).orElseGet(ResumeStatus::new);
        ValidationUtil.isNull( ResumeStatus.getId(),"ResumeStatus","id",resources.getId());
        BeanUtil.copyProperties(resources, ResumeStatus, CopyOptions.create().setIgnoreNullValue(true));
        ResumeStatusRepository.save(ResumeStatus);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            ResumeStatusRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<ResumeStatusDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ResumeStatusDto ResumeStatus : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("到岗时间", ResumeStatus.getArrivalTime());
            map.put("实习时长", ResumeStatus.getInternDuration());
            map.put("每周出勤", ResumeStatus.getWeeklyAttendance());
            map.put(" studentUserId",  ResumeStatus.getStudentUserId());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}