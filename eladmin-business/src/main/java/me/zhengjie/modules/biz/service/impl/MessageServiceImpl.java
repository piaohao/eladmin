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
import me.zhengjie.api.domain.biz.Message;
import me.zhengjie.api.repository.biz.MessageRepository;
import me.zhengjie.modules.biz.service.MessageService;
import me.zhengjie.modules.biz.service.dto.MessageDto;
import me.zhengjie.modules.biz.service.dto.MessageQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.MessageMapper;
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
public class MessageServiceImpl implements MessageService {

    private final MessageRepository MessageRepository;
    private final MessageMapper MessageMapper;

    @Override
    public Map<String,Object> queryAll(MessageQueryCriteria criteria, Pageable pageable){
        Page<Message> page = MessageRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(MessageMapper::toDto));
    }

    @Override
    public List<MessageDto> queryAll(MessageQueryCriteria criteria){
        return MessageMapper.toDto(MessageRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public MessageDto findById(Long id) {
        Message Message = MessageRepository.findById(id).orElseGet(Message::new);
        ValidationUtil.isNull(Message.getId(),"Message","id",id);
        return MessageMapper.toDto(Message);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MessageDto create(Message resources) {
        return MessageMapper.toDto(MessageRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Message resources) {
        Message Message = MessageRepository.findById(resources.getId()).orElseGet(Message::new);
        ValidationUtil.isNull( Message.getId(),"Message","id",resources.getId());
        BeanUtil.copyProperties(resources, Message, CopyOptions.create().setIgnoreNullValue(true));
        MessageRepository.save(Message);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            MessageRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<MessageDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (MessageDto Message : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("消息标题", Message.getTitle());
            map.put("消息类型,1系统公告，2职位消息，3交易消息", Message.getType());
            map.put("消息内容", Message.getContent());
            map.put("发送方式,1所有企业，2部分企业", Message.getDelivery());
            map.put(" createdAt",  Message.getCreatedAt());
            map.put(" updatedAt",  Message.getUpdatedAt());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}