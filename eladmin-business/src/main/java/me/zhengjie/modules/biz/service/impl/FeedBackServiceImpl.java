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

import me.zhengjie.api.domain.biz.FeedBack;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.api.repository.biz.FeedBackRepository;
import me.zhengjie.modules.biz.service.FeedBackService;
import me.zhengjie.modules.biz.service.dto.FeedBackDto;
import me.zhengjie.modules.biz.service.dto.FeedBackQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.FeedBackMapper;
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
public class FeedBackServiceImpl implements FeedBackService {

    private final FeedBackRepository feedBackRepository;
    private final FeedBackMapper feedBackMapper;

    @Override
    public Map<String,Object> queryAll(FeedBackQueryCriteria criteria, Pageable pageable){
        Page<FeedBack> page = feedBackRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(feedBackMapper::toDto));
    }

    @Override
    public List<FeedBackDto> queryAll(FeedBackQueryCriteria criteria){
        return feedBackMapper.toDto(feedBackRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public FeedBackDto findById(Long id) {
        FeedBack feedBack = feedBackRepository.findById(id).orElseGet(FeedBack::new);
        ValidationUtil.isNull(feedBack.getId(),"FeedBack","id",id);
        return feedBackMapper.toDto(feedBack);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FeedBackDto create(FeedBack resources) {
        return feedBackMapper.toDto(feedBackRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(FeedBack resources) {
        FeedBack feedBack = feedBackRepository.findById(resources.getId()).orElseGet(FeedBack::new);
        ValidationUtil.isNull( feedBack.getId(),"FeedBack","id",resources.getId());
        BeanUtil.copyProperties(resources, feedBack, CopyOptions.create().setIgnoreNullValue(true));
        feedBackRepository.save(feedBack);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            feedBackRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<FeedBackDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (FeedBackDto feedBack : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" studentUserId",  feedBack.getStudentUserId());
            map.put(" type",  feedBack.getType());
            map.put(" content",  feedBack.getContent());
            map.put(" mobile",  feedBack.getMobile());
            map.put(" images",  feedBack.getImages());
            map.put(" allowContact",  feedBack.getAllowContact());
            map.put(" createdAt",  feedBack.getCreatedAt());
            map.put(" updatedAt",  feedBack.getUpdatedAt());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}