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

import me.zhengjie.api.domain.biz.CompanyMessage;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.api.repository.biz.CompanyMessageRepository;
import me.zhengjie.modules.biz.service.CompanyMessageService;
import me.zhengjie.modules.biz.service.dto.CompanyMessageDto;
import me.zhengjie.modules.biz.service.dto.CompanyMessageQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.CompanyMessageMapper;
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
public class CompanyMessageServiceImpl implements CompanyMessageService {

    private final CompanyMessageRepository companyMessageRepository;
    private final CompanyMessageMapper companyMessageMapper;

    @Override
    public Map<String,Object> queryAll(CompanyMessageQueryCriteria criteria, Pageable pageable){
        Page<CompanyMessage> page = companyMessageRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(companyMessageMapper::toDto));
    }

    @Override
    public List<CompanyMessageDto> queryAll(CompanyMessageQueryCriteria criteria){
        return companyMessageMapper.toDto(companyMessageRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public CompanyMessageDto findById(Long id) {
        CompanyMessage companyMessage = companyMessageRepository.findById(id).orElseGet(CompanyMessage::new);
        ValidationUtil.isNull(companyMessage.getId(),"CompanyMessage","id",id);
        return companyMessageMapper.toDto(companyMessage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CompanyMessageDto create(CompanyMessage resources) {
        return companyMessageMapper.toDto(companyMessageRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(CompanyMessage resources) {
        CompanyMessage companyMessage = companyMessageRepository.findById(resources.getId()).orElseGet(CompanyMessage::new);
        ValidationUtil.isNull( companyMessage.getId(),"CompanyMessage","id",resources.getId());
        BeanUtil.copyProperties(resources, companyMessage, CopyOptions.create().setIgnoreNullValue(true));
        companyMessageRepository.save(companyMessage);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            companyMessageRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<CompanyMessageDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (CompanyMessageDto companyMessage : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" companyId",  companyMessage.getCompanyId());
            map.put(" messageId",  companyMessage.getMessageId());
            map.put(" isRead",  companyMessage.getIsRead());
            map.put(" createdAt",  companyMessage.getCreatedAt());
            map.put(" updatedAt",  companyMessage.getUpdatedAt());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}