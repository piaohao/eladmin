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

import me.zhengjie.api.domain.biz.Message;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.api.repository.biz.MessageRepository;
import me.zhengjie.modules.biz.service.MessageService;
import me.zhengjie.modules.biz.service.dto.MessageDto;
import me.zhengjie.modules.biz.service.dto.MessageQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.MessageMapper;
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
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    @Override
    public Map<String,Object> queryAll(MessageQueryCriteria criteria, Pageable pageable){
        Page<Message> page = messageRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(messageMapper::toDto));
    }

    @Override
    public List<MessageDto> queryAll(MessageQueryCriteria criteria){
        return messageMapper.toDto(messageRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public MessageDto findById(Long id) {
        Message message = messageRepository.findById(id).orElseGet(Message::new);
        ValidationUtil.isNull(message.getId(),"Message","id",id);
        return messageMapper.toDto(message);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MessageDto create(Message resources) {
        return messageMapper.toDto(messageRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Message resources) {
        Message message = messageRepository.findById(resources.getId()).orElseGet(Message::new);
        ValidationUtil.isNull( message.getId(),"Message","id",resources.getId());
        BeanUtil.copyProperties(resources, message, CopyOptions.create().setIgnoreNullValue(true));
        messageRepository.save(message);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            messageRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<MessageDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (MessageDto message : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("消息标题", message.getTitle());
            map.put("消息类型,1系统公告，2职位消息，3交易消息", message.getType());
            map.put("消息内容", message.getContent());
            map.put("发送方式,1所有企业，2部分企业", message.getDelivery());
            map.put(" createdAt",  message.getCreatedAt());
            map.put(" updatedAt",  message.getUpdatedAt());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}