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
import me.zhengjie.api.domain.biz.SchoolUser;
import me.zhengjie.api.repository.biz.SchoolUserRepository;
import me.zhengjie.modules.biz.service.SchoolUserService;
import me.zhengjie.modules.biz.service.dto.SchoolUserDto;
import me.zhengjie.modules.biz.service.dto.SchoolUserQueryCriteria;
import me.zhengjie.modules.biz.service.mapstruct.SchoolUserMapper;
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
public class SchoolUserServiceImpl implements SchoolUserService {

    private final SchoolUserRepository SchoolUserRepository;
    private final SchoolUserMapper SchoolUserMapper;

    @Override
    public Map<String,Object> queryAll(SchoolUserQueryCriteria criteria, Pageable pageable){
        Page<SchoolUser> page = SchoolUserRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(SchoolUserMapper::toDto));
    }

    @Override
    public List<SchoolUserDto> queryAll(SchoolUserQueryCriteria criteria){
        return SchoolUserMapper.toDto(SchoolUserRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public SchoolUserDto findById(Long id) {
        SchoolUser SchoolUser = SchoolUserRepository.findById(id).orElseGet(SchoolUser::new);
        ValidationUtil.isNull(SchoolUser.getId(),"SchoolUser","id",id);
        return SchoolUserMapper.toDto(SchoolUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SchoolUserDto create(SchoolUser resources) {
        return SchoolUserMapper.toDto(SchoolUserRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SchoolUser resources) {
        SchoolUser SchoolUser = SchoolUserRepository.findById(resources.getId()).orElseGet(SchoolUser::new);
        ValidationUtil.isNull( SchoolUser.getId(),"SchoolUser","id",resources.getId());
        BeanUtil.copyProperties(resources, SchoolUser, CopyOptions.create().setIgnoreNullValue(true));
        SchoolUserRepository.save(SchoolUser);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            SchoolUserRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<SchoolUserDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SchoolUserDto SchoolUser : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" provider",  SchoolUser.getProvider());
            map.put(" uid",  SchoolUser.getUid());
            map.put(" encryptedPassword",  SchoolUser.getEncryptedPassword());
            map.put(" resetPasswordToken",  SchoolUser.getResetPasswordToken());
            map.put(" resetPasswordSentAt",  SchoolUser.getResetPasswordSentAt());
            map.put(" allowPasswordChange",  SchoolUser.getAllowPasswordChange());
            map.put(" rememberCreatedAt",  SchoolUser.getRememberCreatedAt());
            map.put(" confirmationToken",  SchoolUser.getConfirmationToken());
            map.put(" confirmedAt",  SchoolUser.getConfirmedAt());
            map.put(" confirmationSentAt",  SchoolUser.getConfirmationSentAt());
            map.put(" unconfirmedEmail",  SchoolUser.getUnconfirmedEmail());
            map.put(" failedAttempts",  SchoolUser.getFailedAttempts());
            map.put(" unlockToken",  SchoolUser.getUnlockToken());
            map.put(" lockedAt",  SchoolUser.getLockedAt());
            map.put(" signInCount",  SchoolUser.getSignInCount());
            map.put(" currentSignInAt",  SchoolUser.getCurrentSignInAt());
            map.put(" lastSignInAt",  SchoolUser.getLastSignInAt());
            map.put(" currentSignInIp",  SchoolUser.getCurrentSignInIp());
            map.put(" lastSignInIp",  SchoolUser.getLastSignInIp());
            map.put(" name",  SchoolUser.getName());
            map.put(" nickname",  SchoolUser.getNickname());
            map.put(" avatar",  SchoolUser.getAvatar());
            map.put(" email",  SchoolUser.getEmail());
            map.put(" mobile",  SchoolUser.getMobile());
            map.put(" minaOpenid",  SchoolUser.getMinaOpenid());
            map.put(" wxUnionid",  SchoolUser.getWxUnionid());
            map.put(" tokens",  SchoolUser.getTokens());
            map.put(" createdAt",  SchoolUser.getCreatedAt());
            map.put(" updatedAt",  SchoolUser.getUpdatedAt());
            map.put(" schoolId",  SchoolUser.getSchoolId());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}