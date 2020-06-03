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

import me.zhengjie.api.domain.biz.StudentUser;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.api.repository.biz.StudentUserRepository;
import me.zhengjie.modules.biz.service.StudentUserService;
import me.zhengjie.modules.biz.service.dto.StudentUserDto;
import me.zhengjie.modules.biz.service.dto.StudentUserQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.StudentUserMapper;
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
public class StudentUserServiceImpl implements StudentUserService {

    private final StudentUserRepository StudentUserRepository;
    private final StudentUserMapper StudentUserMapper;

    @Override
    public Map<String,Object> queryAll(StudentUserQueryCriteria criteria, Pageable pageable){
        Page<StudentUser> page = StudentUserRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(StudentUserMapper::toDto));
    }

    @Override
    public List<StudentUserDto> queryAll(StudentUserQueryCriteria criteria){
        return StudentUserMapper.toDto(StudentUserRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public StudentUserDto findById(Long id) {
        StudentUser StudentUser = StudentUserRepository.findById(id).orElseGet(StudentUser::new);
        ValidationUtil.isNull(StudentUser.getId(),"StudentUser","id",id);
        return StudentUserMapper.toDto(StudentUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StudentUserDto create(StudentUser resources) {
        return StudentUserMapper.toDto(StudentUserRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(StudentUser resources) {
        StudentUser StudentUser = StudentUserRepository.findById(resources.getId()).orElseGet(StudentUser::new);
        ValidationUtil.isNull( StudentUser.getId(),"StudentUser","id",resources.getId());
        BeanUtil.copyProperties(resources, StudentUser, CopyOptions.create().setIgnoreNullValue(true));
        StudentUserRepository.save(StudentUser);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            StudentUserRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<StudentUserDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (StudentUserDto StudentUser : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" provider",  StudentUser.getProvider());
            map.put(" uid",  StudentUser.getUid());
            map.put(" encryptedPassword",  StudentUser.getEncryptedPassword());
            map.put(" resetPasswordToken",  StudentUser.getResetPasswordToken());
            map.put(" resetPasswordSentAt",  StudentUser.getResetPasswordSentAt());
            map.put(" allowPasswordChange",  StudentUser.getAllowPasswordChange());
            map.put(" rememberCreatedAt",  StudentUser.getRememberCreatedAt());
            map.put(" confirmationToken",  StudentUser.getConfirmationToken());
            map.put(" confirmedAt",  StudentUser.getConfirmedAt());
            map.put(" confirmationSentAt",  StudentUser.getConfirmationSentAt());
            map.put(" unconfirmedEmail",  StudentUser.getUnconfirmedEmail());
            map.put(" failedAttempts",  StudentUser.getFailedAttempts());
            map.put(" unlockToken",  StudentUser.getUnlockToken());
            map.put(" lockedAt",  StudentUser.getLockedAt());
            map.put(" signInCount",  StudentUser.getSignInCount());
            map.put(" currentSignInAt",  StudentUser.getCurrentSignInAt());
            map.put(" lastSignInAt",  StudentUser.getLastSignInAt());
            map.put(" currentSignInIp",  StudentUser.getCurrentSignInIp());
            map.put(" lastSignInIp",  StudentUser.getLastSignInIp());
            map.put(" name",  StudentUser.getName());
            map.put(" nickname",  StudentUser.getNickname());
            map.put(" avatar",  StudentUser.getAvatar());
            map.put(" email",  StudentUser.getEmail());
            map.put(" mobile",  StudentUser.getMobile());
            map.put(" minaOpenid",  StudentUser.getMinaOpenid());
            map.put(" wxUnionid",  StudentUser.getWxUnionid());
            map.put(" tokens",  StudentUser.getTokens());
            map.put(" createdAt",  StudentUser.getCreatedAt());
            map.put(" updatedAt",  StudentUser.getUpdatedAt());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}