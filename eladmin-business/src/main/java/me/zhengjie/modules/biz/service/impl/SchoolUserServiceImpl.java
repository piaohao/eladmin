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

import me.zhengjie.api.domain.biz.SchoolUser;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.api.repository.biz.SchoolUserRepository;
import me.zhengjie.modules.biz.service.SchoolUserService;
import me.zhengjie.modules.biz.service.dto.SchoolUserDto;
import me.zhengjie.modules.biz.service.dto.SchoolUserQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.SchoolUserMapper;
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
public class SchoolUserServiceImpl implements SchoolUserService {

    private final SchoolUserRepository schoolUserRepository;
    private final SchoolUserMapper schoolUserMapper;

    @Override
    public Map<String,Object> queryAll(SchoolUserQueryCriteria criteria, Pageable pageable){
        Page<SchoolUser> page = schoolUserRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(schoolUserMapper::toDto));
    }

    @Override
    public List<SchoolUserDto> queryAll(SchoolUserQueryCriteria criteria){
        return schoolUserMapper.toDto(schoolUserRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public SchoolUserDto findById(Long id) {
        SchoolUser schoolUser = schoolUserRepository.findById(id).orElseGet(SchoolUser::new);
        ValidationUtil.isNull(schoolUser.getId(),"SchoolUser","id",id);
        return schoolUserMapper.toDto(schoolUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SchoolUserDto create(SchoolUser resources) {
        return schoolUserMapper.toDto(schoolUserRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SchoolUser resources) {
        SchoolUser schoolUser = schoolUserRepository.findById(resources.getId()).orElseGet(SchoolUser::new);
        ValidationUtil.isNull( schoolUser.getId(),"SchoolUser","id",resources.getId());
        BeanUtil.copyProperties(resources, schoolUser, CopyOptions.create().setIgnoreNullValue(true));
        schoolUserRepository.save(schoolUser);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            schoolUserRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<SchoolUserDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SchoolUserDto schoolUser : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" provider",  schoolUser.getProvider());
            map.put(" uid",  schoolUser.getUid());
            map.put(" encryptedPassword",  schoolUser.getEncryptedPassword());
            map.put(" resetPasswordToken",  schoolUser.getResetPasswordToken());
            map.put(" resetPasswordSentAt",  schoolUser.getResetPasswordSentAt());
            map.put(" allowPasswordChange",  schoolUser.getAllowPasswordChange());
            map.put(" rememberCreatedAt",  schoolUser.getRememberCreatedAt());
            map.put(" confirmationToken",  schoolUser.getConfirmationToken());
            map.put(" confirmedAt",  schoolUser.getConfirmedAt());
            map.put(" confirmationSentAt",  schoolUser.getConfirmationSentAt());
            map.put(" unconfirmedEmail",  schoolUser.getUnconfirmedEmail());
            map.put(" failedAttempts",  schoolUser.getFailedAttempts());
            map.put(" unlockToken",  schoolUser.getUnlockToken());
            map.put(" lockedAt",  schoolUser.getLockedAt());
            map.put(" signInCount",  schoolUser.getSignInCount());
            map.put(" currentSignInAt",  schoolUser.getCurrentSignInAt());
            map.put(" lastSignInAt",  schoolUser.getLastSignInAt());
            map.put(" currentSignInIp",  schoolUser.getCurrentSignInIp());
            map.put(" lastSignInIp",  schoolUser.getLastSignInIp());
            map.put(" name",  schoolUser.getName());
            map.put(" nickname",  schoolUser.getNickname());
            map.put(" avatar",  schoolUser.getAvatar());
            map.put(" email",  schoolUser.getEmail());
            map.put(" mobile",  schoolUser.getMobile());
            map.put(" minaOpenid",  schoolUser.getMinaOpenid());
            map.put(" wxUnionid",  schoolUser.getWxUnionid());
            map.put(" tokens",  schoolUser.getTokens());
            map.put(" createdAt",  schoolUser.getCreatedAt());
            map.put(" updatedAt",  schoolUser.getUpdatedAt());
            map.put(" schoolId",  schoolUser.getSchoolId());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}