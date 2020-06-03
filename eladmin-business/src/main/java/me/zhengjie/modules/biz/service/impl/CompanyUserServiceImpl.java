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
import me.zhengjie.api.domain.biz.CompanyUser;
import me.zhengjie.api.repository.biz.CompanyUserRepository;
import me.zhengjie.modules.biz.service.CompanyUserService;
import me.zhengjie.modules.biz.service.dto.CompanyUserDto;
import me.zhengjie.modules.biz.service.dto.CompanyUserQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.CompanyUserMapper;
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
public class CompanyUserServiceImpl implements CompanyUserService {

    private final CompanyUserRepository CompanyUserRepository;
    private final CompanyUserMapper CompanyUserMapper;

    @Override
    public Map<String,Object> queryAll(CompanyUserQueryCriteria criteria, Pageable pageable){
        Page<CompanyUser> page = CompanyUserRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(CompanyUserMapper::toDto));
    }

    @Override
    public List<CompanyUserDto> queryAll(CompanyUserQueryCriteria criteria){
        return CompanyUserMapper.toDto(CompanyUserRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public CompanyUserDto findById(Long id) {
        CompanyUser CompanyUser = CompanyUserRepository.findById(id).orElseGet(CompanyUser::new);
        ValidationUtil.isNull(CompanyUser.getId(),"CompanyUser","id",id);
        return CompanyUserMapper.toDto(CompanyUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CompanyUserDto create(CompanyUser resources) {
        return CompanyUserMapper.toDto(CompanyUserRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(CompanyUser resources) {
        CompanyUser CompanyUser = CompanyUserRepository.findById(resources.getId()).orElseGet(CompanyUser::new);
        ValidationUtil.isNull( CompanyUser.getId(),"CompanyUser","id",resources.getId());
        BeanUtil.copyProperties(resources, CompanyUser, CopyOptions.create().setIgnoreNullValue(true));
        CompanyUserRepository.save(CompanyUser);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            CompanyUserRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<CompanyUserDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (CompanyUserDto CompanyUser : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" provider",  CompanyUser.getProvider());
            map.put(" uid",  CompanyUser.getUid());
            map.put(" encryptedPassword",  CompanyUser.getEncryptedPassword());
            map.put(" resetPasswordToken",  CompanyUser.getResetPasswordToken());
            map.put(" resetPasswordSentAt",  CompanyUser.getResetPasswordSentAt());
            map.put(" allowPasswordChange",  CompanyUser.getAllowPasswordChange());
            map.put(" rememberCreatedAt",  CompanyUser.getRememberCreatedAt());
            map.put(" confirmationToken",  CompanyUser.getConfirmationToken());
            map.put(" confirmedAt",  CompanyUser.getConfirmedAt());
            map.put(" confirmationSentAt",  CompanyUser.getConfirmationSentAt());
            map.put(" unconfirmedEmail",  CompanyUser.getUnconfirmedEmail());
            map.put(" failedAttempts",  CompanyUser.getFailedAttempts());
            map.put(" unlockToken",  CompanyUser.getUnlockToken());
            map.put(" lockedAt",  CompanyUser.getLockedAt());
            map.put(" signInCount",  CompanyUser.getSignInCount());
            map.put(" currentSignInAt",  CompanyUser.getCurrentSignInAt());
            map.put(" lastSignInAt",  CompanyUser.getLastSignInAt());
            map.put(" currentSignInIp",  CompanyUser.getCurrentSignInIp());
            map.put(" lastSignInIp",  CompanyUser.getLastSignInIp());
            map.put(" name",  CompanyUser.getName());
            map.put(" nickname",  CompanyUser.getNickname());
            map.put(" avatar",  CompanyUser.getAvatar());
            map.put(" email",  CompanyUser.getEmail());
            map.put(" mobile",  CompanyUser.getMobile());
            map.put(" minaOpenid",  CompanyUser.getMinaOpenid());
            map.put(" wxUnionid",  CompanyUser.getWxUnionid());
            map.put(" tokens",  CompanyUser.getTokens());
            map.put(" createdAt",  CompanyUser.getCreatedAt());
            map.put(" updatedAt",  CompanyUser.getUpdatedAt());
            map.put(" companyId",  CompanyUser.getCompanyId());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}