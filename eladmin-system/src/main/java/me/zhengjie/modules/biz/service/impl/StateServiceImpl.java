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

import me.zhengjie.api.domain.biz.State;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.api.repository.biz.StateRepository;
import me.zhengjie.modules.biz.service.StateService;
import me.zhengjie.modules.biz.service.dto.StateDto;
import me.zhengjie.modules.biz.service.dto.StateQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.StateMapper;
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
public class StateServiceImpl implements StateService {

    private final StateRepository StateRepository;
    private final StateMapper StateMapper;

    @Override
    public Map<String,Object> queryAll(StateQueryCriteria criteria, Pageable pageable){
        Page<State> page = StateRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(StateMapper::toDto));
    }

    @Override
    public List<StateDto> queryAll(StateQueryCriteria criteria){
        return StateMapper.toDto(StateRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public StateDto findById(Long id) {
        State State = StateRepository.findById(id).orElseGet(State::new);
        ValidationUtil.isNull(State.getId(),"State","id",id);
        return StateMapper.toDto(State);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StateDto create(State resources) {
        return StateMapper.toDto(StateRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(State resources) {
        State State = StateRepository.findById(resources.getId()).orElseGet(State::new);
        ValidationUtil.isNull( State.getId(),"State","id",resources.getId());
        BeanUtil.copyProperties(resources, State, CopyOptions.create().setIgnoreNullValue(true));
        StateRepository.save(State);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            StateRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<StateDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (StateDto State : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" name",  State.getName());
            map.put(" createdAt",  State.getCreatedAt());
            map.put(" updatedAt",  State.getUpdatedAt());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}