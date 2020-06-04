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
public class AdminUserServiceImpl implements AdminUserService {

    private final AdminUserRepository adminUserRepository;
    private final AdminUserMapper adminUserMapper;

    @Override
    public Map<String,Object> queryAll(AdminUserQueryCriteria criteria, Pageable pageable){
        Page<AdminUser> page = adminUserRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(adminUserMapper::toDto));
    }

    @Override
    public List<AdminUserDto> queryAll(AdminUserQueryCriteria criteria){
        return adminUserMapper.toDto(adminUserRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public AdminUserDto findById(Long id) {
        AdminUser adminUser = adminUserRepository.findById(id).orElseGet(AdminUser::new);
        ValidationUtil.isNull(adminUser.getId(),"AdminUser","id",id);
        return adminUserMapper.toDto(adminUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AdminUserDto create(AdminUser resources) {
        return adminUserMapper.toDto(adminUserRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AdminUser resources) {
        AdminUser adminUser = adminUserRepository.findById(resources.getId()).orElseGet(AdminUser::new);
        ValidationUtil.isNull( adminUser.getId(),"AdminUser","id",resources.getId());
        BeanUtil.copyProperties(resources, adminUser, CopyOptions.create().setIgnoreNullValue(true));
        adminUserRepository.save(adminUser);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            adminUserRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<AdminUserDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (AdminUserDto adminUser : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" provider",  adminUser.getProvider());
            map.put(" uid",  adminUser.getUid());
            map.put(" encryptedPassword",  adminUser.getEncryptedPassword());
            map.put(" resetPasswordToken",  adminUser.getResetPasswordToken());
            map.put(" resetPasswordSentAt",  adminUser.getResetPasswordSentAt());
            map.put(" allowPasswordChange",  adminUser.getAllowPasswordChange());
            map.put(" rememberCreatedAt",  adminUser.getRememberCreatedAt());
            map.put(" confirmationToken",  adminUser.getConfirmationToken());
            map.put(" confirmedAt",  adminUser.getConfirmedAt());
            map.put(" confirmationSentAt",  adminUser.getConfirmationSentAt());
            map.put(" unconfirmedEmail",  adminUser.getUnconfirmedEmail());
            map.put(" failedAttempts",  adminUser.getFailedAttempts());
            map.put(" unlockToken",  adminUser.getUnlockToken());
            map.put(" lockedAt",  adminUser.getLockedAt());
            map.put(" signInCount",  adminUser.getSignInCount());
            map.put(" currentSignInAt",  adminUser.getCurrentSignInAt());
            map.put(" lastSignInAt",  adminUser.getLastSignInAt());
            map.put(" currentSignInIp",  adminUser.getCurrentSignInIp());
            map.put(" lastSignInIp",  adminUser.getLastSignInIp());
            map.put(" name",  adminUser.getName());
            map.put(" nickname",  adminUser.getNickname());
            map.put(" avatar",  adminUser.getAvatar());
            map.put(" email",  adminUser.getEmail());
            map.put(" mobile",  adminUser.getMobile());
            map.put(" minaOpenid",  adminUser.getMinaOpenid());
            map.put(" wxUnionid",  adminUser.getWxUnionid());
            map.put(" tokens",  adminUser.getTokens());
            map.put(" createdAt",  adminUser.getCreatedAt());
            map.put(" updatedAt",  adminUser.getUpdatedAt());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}