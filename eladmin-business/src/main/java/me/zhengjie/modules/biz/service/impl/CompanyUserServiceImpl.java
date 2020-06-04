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

import me.zhengjie.api.domain.biz.CompanyUser;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.api.repository.biz.CompanyUserRepository;
import me.zhengjie.modules.biz.service.CompanyUserService;
import me.zhengjie.modules.biz.service.dto.CompanyUserDto;
import me.zhengjie.modules.biz.service.dto.CompanyUserQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.CompanyUserMapper;
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
public class CompanyUserServiceImpl implements CompanyUserService {

    private final CompanyUserRepository companyUserRepository;
    private final CompanyUserMapper companyUserMapper;

    @Override
    public Map<String,Object> queryAll(CompanyUserQueryCriteria criteria, Pageable pageable){
        Page<CompanyUser> page = companyUserRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(companyUserMapper::toDto));
    }

    @Override
    public List<CompanyUserDto> queryAll(CompanyUserQueryCriteria criteria){
        return companyUserMapper.toDto(companyUserRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public CompanyUserDto findById(Long id) {
        CompanyUser companyUser = companyUserRepository.findById(id).orElseGet(CompanyUser::new);
        ValidationUtil.isNull(companyUser.getId(),"CompanyUser","id",id);
        return companyUserMapper.toDto(companyUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CompanyUserDto create(CompanyUser resources) {
        return companyUserMapper.toDto(companyUserRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(CompanyUser resources) {
        CompanyUser companyUser = companyUserRepository.findById(resources.getId()).orElseGet(CompanyUser::new);
        ValidationUtil.isNull( companyUser.getId(),"CompanyUser","id",resources.getId());
        BeanUtil.copyProperties(resources, companyUser, CopyOptions.create().setIgnoreNullValue(true));
        companyUserRepository.save(companyUser);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            companyUserRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<CompanyUserDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (CompanyUserDto companyUser : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" provider",  companyUser.getProvider());
            map.put(" uid",  companyUser.getUid());
            map.put(" encryptedPassword",  companyUser.getEncryptedPassword());
            map.put(" resetPasswordToken",  companyUser.getResetPasswordToken());
            map.put(" resetPasswordSentAt",  companyUser.getResetPasswordSentAt());
            map.put(" allowPasswordChange",  companyUser.getAllowPasswordChange());
            map.put(" rememberCreatedAt",  companyUser.getRememberCreatedAt());
            map.put(" confirmationToken",  companyUser.getConfirmationToken());
            map.put(" confirmedAt",  companyUser.getConfirmedAt());
            map.put(" confirmationSentAt",  companyUser.getConfirmationSentAt());
            map.put(" unconfirmedEmail",  companyUser.getUnconfirmedEmail());
            map.put(" failedAttempts",  companyUser.getFailedAttempts());
            map.put(" unlockToken",  companyUser.getUnlockToken());
            map.put(" lockedAt",  companyUser.getLockedAt());
            map.put(" signInCount",  companyUser.getSignInCount());
            map.put(" currentSignInAt",  companyUser.getCurrentSignInAt());
            map.put(" lastSignInAt",  companyUser.getLastSignInAt());
            map.put(" currentSignInIp",  companyUser.getCurrentSignInIp());
            map.put(" lastSignInIp",  companyUser.getLastSignInIp());
            map.put(" name",  companyUser.getName());
            map.put(" nickname",  companyUser.getNickname());
            map.put(" avatar",  companyUser.getAvatar());
            map.put(" email",  companyUser.getEmail());
            map.put(" mobile",  companyUser.getMobile());
            map.put(" minaOpenid",  companyUser.getMinaOpenid());
            map.put(" wxUnionid",  companyUser.getWxUnionid());
            map.put(" tokens",  companyUser.getTokens());
            map.put(" createdAt",  companyUser.getCreatedAt());
            map.put(" updatedAt",  companyUser.getUpdatedAt());
            map.put(" companyId",  companyUser.getCompanyId());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}