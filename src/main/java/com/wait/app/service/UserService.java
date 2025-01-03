package com.wait.app.service;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONUtil;

import com.wait.app.domain.dto.role.RoleDTO;
import com.wait.app.domain.dto.user.UserInfoDTO;
import com.wait.app.domain.entity.User;
import com.wait.app.domain.entity.UserRole;
import com.wait.app.repository.UserRepository;
import com.wait.app.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author 天
 * Time: 2024/9/15 10:54
 */
@Service
public class UserService {

    private final UserRoleRepository userRoleRepository;

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRoleRepository userRoleRepository, UserRepository userRepository) {
        this.userRoleRepository = userRoleRepository;
        this.userRepository = userRepository;
    }

    /**
     * 分配角色
     * @param userId userId
     * @param roleIds roleIds
     */
    public void distribute(String userId, List<String> roleIds) {
        userRoleRepository.removeByUserId(userId);
        List<UserRole> list = roleIds.stream().map(item -> UserRole.builder().userId(userId).roleId(item).build()).toList();
        userRoleRepository.saveBatch(list);
    }

    /**
     * 用户信息
     * @return UserInfoDTO
     */
    public UserInfoDTO info() {
        SaSession session = StpUtil.getSession();
        User user = JSONUtil.toBean((String) session.get(SaSession.USER),User.class);
        List<RoleDTO> roleDTOList = JSONUtil.toList((String) session.get(SaSession.ROLE_LIST), RoleDTO.class);
        return UserInfoDTO.builder().id(user.getId()).roles(roleDTOList).build();
    }

    public User getUserByPhone(String phone) {
        return userRepository.lambdaQuery().eq(User::getPhone,phone).one();
    }
}
