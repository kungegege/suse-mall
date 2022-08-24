package com.frame.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.frame.business.entity.Role;
import com.frame.business.entity.UserRole;
import com.frame.business.mapper.RoleMapper;
import com.frame.business.mapper.UserRoleMapper;
import com.frame.business.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色 服务实现类
 * </p>
 *
 * @author 邹坤
 * @since 2022-04-10
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Resource
    private UserRoleMapper userRoleMapper;

    @Override
    public List<Role> getByUserId(String userId) {

        if (StringUtils.isEmpty(userId)) {
            return Collections.emptyList();
        }

        QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<>();
        userRoleQueryWrapper.select("role_id").eq("user_id", userId);

        List<UserRole> userRoles = userRoleMapper.selectList(userRoleQueryWrapper);

        if (userRoles.isEmpty()) {
            return Collections.emptyList();
        }

        List<Integer> roleIds = userRoles.stream().map(it -> it.getRoleId()).collect(Collectors.toList());

        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("role_id", roleIds);

        return baseMapper.selectList(queryWrapper);
    }



    @Override
    public void insert(String userId, Integer roleId) {
        UserRole userRole = new UserRole(null, userId, roleId);
        userRoleMapper.insert(userRole);
    }


    @Override
    public List<Role> getAllRoles() {
        return baseMapper.selectList(null);
    }
}
