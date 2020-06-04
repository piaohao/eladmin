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
public class StudentUserServiceImpl implements StudentUserService {

    private final StudentUserRepository studentUserRepository;
    private final StudentUserMapper studentUserMapper;

    @Override
    public Map<String,Object> queryAll(StudentUserQueryCriteria criteria, Pageable pageable){
        Page<StudentUser> page = studentUserRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(studentUserMapper::toDto));
    }

    @Override
    public List<StudentUserDto> queryAll(StudentUserQueryCriteria criteria){
        return studentUserMapper.toDto(studentUserRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public StudentUserDto findById(Long id) {
        StudentUser studentUser = studentUserRepository.findById(id).orElseGet(StudentUser::new);
        ValidationUtil.isNull(studentUser.getId(),"StudentUser","id",id);
        return studentUserMapper.toDto(studentUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StudentUserDto create(StudentUser resources) {
        return studentUserMapper.toDto(studentUserRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(StudentUser resources) {
        StudentUser studentUser = studentUserRepository.findById(resources.getId()).orElseGet(StudentUser::new);
        ValidationUtil.isNull( studentUser.getId(),"StudentUser","id",resources.getId());
        BeanUtil.copyProperties(resources, studentUser, CopyOptions.create().setIgnoreNullValue(true));
        studentUserRepository.save(studentUser);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            studentUserRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<StudentUserDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (StudentUserDto studentUser : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" provider",  studentUser.getProvider());
            map.put(" uid",  studentUser.getUid());
            map.put(" encryptedPassword",  studentUser.getEncryptedPassword());
            map.put(" resetPasswordToken",  studentUser.getResetPasswordToken());
            map.put(" resetPasswordSentAt",  studentUser.getResetPasswordSentAt());
            map.put(" allowPasswordChange",  studentUser.getAllowPasswordChange());
            map.put(" rememberCreatedAt",  studentUser.getRememberCreatedAt());
            map.put(" confirmationToken",  studentUser.getConfirmationToken());
            map.put(" confirmedAt",  studentUser.getConfirmedAt());
            map.put(" confirmationSentAt",  studentUser.getConfirmationSentAt());
            map.put(" unconfirmedEmail",  studentUser.getUnconfirmedEmail());
            map.put(" failedAttempts",  studentUser.getFailedAttempts());
            map.put(" unlockToken",  studentUser.getUnlockToken());
            map.put(" lockedAt",  studentUser.getLockedAt());
            map.put(" signInCount",  studentUser.getSignInCount());
            map.put(" currentSignInAt",  studentUser.getCurrentSignInAt());
            map.put(" lastSignInAt",  studentUser.getLastSignInAt());
            map.put(" currentSignInIp",  studentUser.getCurrentSignInIp());
            map.put(" lastSignInIp",  studentUser.getLastSignInIp());
            map.put(" name",  studentUser.getName());
            map.put(" nickname",  studentUser.getNickname());
            map.put(" avatar",  studentUser.getAvatar());
            map.put(" email",  studentUser.getEmail());
            map.put(" mobile",  studentUser.getMobile());
            map.put(" minaOpenid",  studentUser.getMinaOpenid());
            map.put(" wxUnionid",  studentUser.getWxUnionid());
            map.put(" tokens",  studentUser.getTokens());
            map.put(" createdAt",  studentUser.getCreatedAt());
            map.put(" updatedAt",  studentUser.getUpdatedAt());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}