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
public class FeedBackServiceImpl implements FeedBackService {

    private final FeedBackRepository FeedBackRepository;
    private final FeedBackMapper FeedBackMapper;

    @Override
    public Map<String,Object> queryAll(FeedBackQueryCriteria criteria, Pageable pageable){
        Page<FeedBack> page = FeedBackRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(FeedBackMapper::toDto));
    }

    @Override
    public List<FeedBackDto> queryAll(FeedBackQueryCriteria criteria){
        return FeedBackMapper.toDto(FeedBackRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public FeedBackDto findById(Long id) {
        FeedBack FeedBack = FeedBackRepository.findById(id).orElseGet(FeedBack::new);
        ValidationUtil.isNull(FeedBack.getId(),"FeedBack","id",id);
        return FeedBackMapper.toDto(FeedBack);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FeedBackDto create(FeedBack resources) {
        return FeedBackMapper.toDto(FeedBackRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(FeedBack resources) {
        FeedBack FeedBack = FeedBackRepository.findById(resources.getId()).orElseGet(FeedBack::new);
        ValidationUtil.isNull( FeedBack.getId(),"FeedBack","id",resources.getId());
        BeanUtil.copyProperties(resources, FeedBack, CopyOptions.create().setIgnoreNullValue(true));
        FeedBackRepository.save(FeedBack);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            FeedBackRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<FeedBackDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (FeedBackDto FeedBack : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" studentUserId",  FeedBack.getStudentUserId());
            map.put(" type",  FeedBack.getType());
            map.put(" content",  FeedBack.getContent());
            map.put(" mobile",  FeedBack.getMobile());
            map.put(" images",  FeedBack.getImages());
            map.put(" allowContact",  FeedBack.getAllowContact());
            map.put(" createdAt",  FeedBack.getCreatedAt());
            map.put(" updatedAt",  FeedBack.getUpdatedAt());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}