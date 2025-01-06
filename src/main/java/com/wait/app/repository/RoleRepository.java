package com.wait.app.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.tangzc.mpe.base.repository.BaseRepository;
import com.wait.app.domain.dto.role.RoleDTO;
import com.wait.app.domain.entity.Role;
import com.wait.app.repository.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 天
 * Time: 2024/9/15 9:30
 */
@Repository
public class RoleRepository extends BaseRepository<RoleMapper, Role> {

    private final RoleMapper roleMapper;

    @Autowired
    public RoleRepository(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    /**
     * 根据level获取角色
     *
     * @param roleKey roleKey
     * @return 角色
     */
    public Role getRoleByLevel(Integer roleKey) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Role::getRoleKey,roleKey);
        return roleMapper.selectOne(queryWrapper);
    }

    /**
     * 根据userId获取角色
     * @param userId userId
     * @return 角色
     */
    public List<RoleDTO> getRoleByUserId(String userId) {
        return roleMapper.getRoleByUserId(userId);
    }
}
