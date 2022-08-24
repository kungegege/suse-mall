package com.frame.business.service;

import com.frame.business.entity.Role;
import com.frame.business.entity.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 菜单管理 服务类
 * </p>
 *
 * @author 邹坤
 * @since 2022-04-13
 */
public interface SysMenuService extends IService<SysMenu> {

    List<SysMenu> selectAll();

    List<SysMenu> selectList(String id);

    List<Role> queryMenuFormRole();
}
