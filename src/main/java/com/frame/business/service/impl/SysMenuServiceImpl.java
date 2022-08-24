package com.frame.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.frame.business.entity.MenuRole;
import com.frame.business.entity.Role;
import com.frame.business.entity.SysMenu;
import com.frame.business.entity.UserRole;
import com.frame.business.mapper.SysMenuMapper;
import com.frame.business.service.MenuRoleService;
import com.frame.business.service.RoleService;
import com.frame.business.service.SysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.frame.business.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单管理 服务实现类
 * </p>
 *
 * @author 邹坤
 * @since 2022-04-13
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {


    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuRoleService menuRoleService;

    @Override
    public List<SysMenu> selectAll() {
        return baseMapper.selectList(null);
    }

    @Override
    public List<SysMenu> selectList(String id) {
        List<Role> roles = roleService.getByUserId(id);
        List<Integer> roleIds = roles.stream().map(it -> it.getRoleId()).collect(Collectors.toList());
        List<MenuRole> menuRoles = menuRoleService.selectByRolesId(roleIds);
        List<Integer> menuIds = menuRoles.stream().map(it -> it.getMenuId()).collect(Collectors.toList());
        if (menuIds.isEmpty()){
            return Collections.emptyList();
        }
        QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<>();

        queryWrapper.in("id", menuIds);

        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<Role> queryMenuFormRole() {
        List<Role> allRoles = roleService.getAllRoles();

        return allRoles.stream().map(role -> {
            List<SysMenu> sysMenus = getSysMenuByRoleId(role.getRoleId());
            role.setMenus(toTree(sysMenus, 0));
            return role;
        }).collect(Collectors.toList());
    }

    public List<SysMenu> getSysMenuByRoleId(Integer roleId) {
        List<MenuRole> menuRoles = menuRoleService.selectByRoleId(roleId);
        HashSet<SysMenu> sysMenus = new HashSet<>();

        for (MenuRole menuRole : menuRoles) {
            QueryWrapper<SysMenu> sysMenuQueryWrapper = new QueryWrapper<>();
            sysMenuQueryWrapper.eq("id", menuRole.getMenuId());
            SysMenu menu = baseMapper.selectOne(sysMenuQueryWrapper);
            sysMenus.add(menu);
        }

        ArrayList<SysMenu> result = new ArrayList<>(sysMenus);
        return result;
    }


    private  List<SysMenu> toTree(List<SysMenu> menus, int rootFlag) {
        return menus.stream()
                .filter(it -> it!=null && it.getParentId() == rootFlag)
                .peek(it -> it.setChild(getChildren(menus, it)))
                .collect(Collectors.toList());
    }

    private List<SysMenu> getChildren(List<SysMenu> menus, SysMenu sysMenu) {
        List<SysMenu> children = menus.stream()
                .filter(it -> it!=null  && it.getParentId().equals(sysMenu.getId()))
                .map(it -> {
                    List<SysMenu> list = getChildren(menus, it);
                    if (!list.isEmpty()) {
                        it.setChild(list);
                    }
                    return it;
                }).collect(Collectors.toList());
        return children;
    }
}
