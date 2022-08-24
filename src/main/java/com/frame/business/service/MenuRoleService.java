package com.frame.business.service;

import com.frame.business.entity.MenuRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.frame.business.entity.SysMenu;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 邹坤
 * @since 2022-04-13
 */
public interface MenuRoleService extends IService<MenuRole> {

    List<MenuRole> selectByRolesId(List<Integer> roleIds);

    List<MenuRole> selectByRoleId(Integer roleId);

}
