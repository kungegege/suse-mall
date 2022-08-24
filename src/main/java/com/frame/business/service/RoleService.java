package com.frame.business.service;

import com.frame.business.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色 服务类
 * </p>
 *
 * @author 邹坤
 * @since 2022-04-10
 */
public interface RoleService extends IService<Role> {

    List<Role> getByUserId(String userId);

    void insert(String userId, Integer roleId);

    List<Role> getAllRoles();
}
