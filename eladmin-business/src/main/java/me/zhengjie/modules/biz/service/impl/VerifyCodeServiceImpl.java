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

import me.zhengjie.api.domain.biz.VerifyCode;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.api.repository.biz.VerifyCodeRepository;
import me.zhengjie.modules.biz.service.VerifyCodeService;
import me.zhengjie.modules.biz.service.dto.VerifyCodeDto;
import me.zhengjie.modules.biz.service.dto.VerifyCodeQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.VerifyCodeMapper;
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
public class VerifyCodeServiceImpl implements VerifyCodeService {

    private final VerifyCodeRepository verifyCodeRepository;
    private final VerifyCodeMapper verifyCodeMapper;

    @Override
    public Map<String,Object> queryAll(VerifyCodeQueryCriteria criteria, Pageable pageable){
        Page<VerifyCode> page = verifyCodeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(verifyCodeMapper::toDto));
    }

    @Override
    public List<VerifyCodeDto> queryAll(VerifyCodeQueryCriteria criteria){
        return verifyCodeMapper.toDto(verifyCodeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public VerifyCodeDto findById(Long id) {
        VerifyCode verifyCode = verifyCodeRepository.findById(id).orElseGet(VerifyCode::new);
        ValidationUtil.isNull(verifyCode.getId(),"VerifyCode","id",id);
        return verifyCodeMapper.toDto(verifyCode);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VerifyCodeDto create(VerifyCode resources) {
        return verifyCodeMapper.toDto(verifyCodeRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(VerifyCode resources) {
        VerifyCode verifyCode = verifyCodeRepository.findById(resources.getId()).orElseGet(VerifyCode::new);
        ValidationUtil.isNull( verifyCode.getId(),"VerifyCode","id",resources.getId());
        BeanUtil.copyProperties(resources, verifyCode, CopyOptions.create().setIgnoreNullValue(true));
        verifyCodeRepository.save(verifyCode);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            verifyCodeRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<VerifyCodeDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (VerifyCodeDto verifyCode : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" mobile",  verifyCode.getMobile());
            map.put(" chn",  verifyCode.getChn());
            map.put(" val",  verifyCode.getVal());
            map.put(" expAt",  verifyCode.getExpAt());
            map.put(" createdAt",  verifyCode.getCreatedAt());
            map.put(" updatedAt",  verifyCode.getUpdatedAt());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}