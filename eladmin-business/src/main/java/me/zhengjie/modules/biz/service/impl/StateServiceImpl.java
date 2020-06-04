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
public class StateServiceImpl implements StateService {

    private final StateRepository stateRepository;
    private final StateMapper stateMapper;

    @Override
    public Map<String,Object> queryAll(StateQueryCriteria criteria, Pageable pageable){
        Page<State> page = stateRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(stateMapper::toDto));
    }

    @Override
    public List<StateDto> queryAll(StateQueryCriteria criteria){
        return stateMapper.toDto(stateRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public StateDto findById(Long id) {
        State state = stateRepository.findById(id).orElseGet(State::new);
        ValidationUtil.isNull(state.getId(),"State","id",id);
        return stateMapper.toDto(state);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StateDto create(State resources) {
        return stateMapper.toDto(stateRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(State resources) {
        State state = stateRepository.findById(resources.getId()).orElseGet(State::new);
        ValidationUtil.isNull( state.getId(),"State","id",resources.getId());
        BeanUtil.copyProperties(resources, state, CopyOptions.create().setIgnoreNullValue(true));
        stateRepository.save(state);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            stateRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<StateDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (StateDto state : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" name",  state.getName());
            map.put(" createdAt",  state.getCreatedAt());
            map.put(" updatedAt",  state.getUpdatedAt());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}