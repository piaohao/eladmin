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

import me.zhengjie.api.domain.biz.AdminUser;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.api.repository.biz.AdminUserRepository;
import me.zhengjie.modules.biz.service.AdminUserService;
import me.zhengjie.modules.biz.service.dto.AdminUserDto;
import me.zhengjie.modules.biz.service.dto.AdminUserQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.AdminUserMapper;
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
public class AdminUserServiceImpl implements AdminUserService {

    private final AdminUserRepository AdminUserRepository;
    private final AdminUserMapper AdminUserMapper;

    @Override
    public Map<String,Object> queryAll(AdminUserQueryCriteria criteria, Pageable pageable){
        Page<AdminUser> page = AdminUserRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(AdminUserMapper::toDto));
    }

    @Override
    public List<AdminUserDto> queryAll(AdminUserQueryCriteria criteria){
        return AdminUserMapper.toDto(AdminUserRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public AdminUserDto findById(Long id) {
        AdminUser AdminUser = AdminUserRepository.findById(id).orElseGet(AdminUser::new);
        ValidationUtil.isNull(AdminUser.getId(),"AdminUser","id",id);
        return AdminUserMapper.toDto(AdminUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AdminUserDto create(AdminUser resources) {
        return AdminUserMapper.toDto(AdminUserRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AdminUser resources) {
        AdminUser AdminUser = AdminUserRepository.findById(resources.getId()).orElseGet(AdminUser::new);
        ValidationUtil.isNull( AdminUser.getId(),"AdminUser","id",resources.getId());
        BeanUtil.copyProperties(resources, AdminUser, CopyOptions.create().setIgnoreNullValue(true));
        AdminUserRepository.save(AdminUser);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            AdminUserRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<AdminUserDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (AdminUserDto AdminUser : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" provider",  AdminUser.getProvider());
            map.put(" uid",  AdminUser.getUid());
            map.put(" encryptedPassword",  AdminUser.getEncryptedPassword());
            map.put(" resetPasswordToken",  AdminUser.getResetPasswordToken());
            map.put(" resetPasswordSentAt",  AdminUser.getResetPasswordSentAt());
            map.put(" allowPasswordChange",  AdminUser.getAllowPasswordChange());
            map.put(" rememberCreatedAt",  AdminUser.getRememberCreatedAt());
            map.put(" confirmationToken",  AdminUser.getConfirmationToken());
            map.put(" confirmedAt",  AdminUser.getConfirmedAt());
            map.put(" confirmationSentAt",  AdminUser.getConfirmationSentAt());
            map.put(" unconfirmedEmail",  AdminUser.getUnconfirmedEmail());
            map.put(" failedAttempts",  AdminUser.getFailedAttempts());
            map.put(" unlockToken",  AdminUser.getUnlockToken());
            map.put(" lockedAt",  AdminUser.getLockedAt());
            map.put(" signInCount",  AdminUser.getSignInCount());
            map.put(" currentSignInAt",  AdminUser.getCurrentSignInAt());
            map.put(" lastSignInAt",  AdminUser.getLastSignInAt());
            map.put(" currentSignInIp",  AdminUser.getCurrentSignInIp());
            map.put(" lastSignInIp",  AdminUser.getLastSignInIp());
            map.put(" name",  AdminUser.getName());
            map.put(" nickname",  AdminUser.getNickname());
            map.put(" avatar",  AdminUser.getAvatar());
            map.put(" email",  AdminUser.getEmail());
            map.put(" mobile",  AdminUser.getMobile());
            map.put(" minaOpenid",  AdminUser.getMinaOpenid());
            map.put(" wxUnionid",  AdminUser.getWxUnionid());
            map.put(" tokens",  AdminUser.getTokens());
            map.put(" createdAt",  AdminUser.getCreatedAt());
            map.put(" updatedAt",  AdminUser.getUpdatedAt());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}