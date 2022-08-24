package com.frame.business.controller;


import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import com.frame.annotation.RequiresRoles;
import com.frame.business.base.BaseController;
import com.frame.business.entity.Role;
import com.frame.business.entity.SysMenu;
import com.frame.business.entity.UserRole;
import com.frame.business.service.SysMenuService;
import com.frame.contants.Constant;
import com.frame.utils.DataResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单管理 前端控制器
 * </p>
 * @author 邹坤
 * @since 2022-04-13
 */
@RestController
@RequestMapping("/menu")
public class SysMenuController extends BaseController {

    @Autowired
    private SysMenuService sysMenuService;

    @GetMapping("get/all")
    public DataResult getAllMenu() {
        List<SysMenu> list = sysMenuService.selectAll();
        List<SysMenu> trees = toTree(list,0);
        return DataResult.success(trees);
    }

    @GetMapping("get")
    @RequiresRoles("user")
    public DataResult getMenu() {
        String id = getCurrentUserId();
        List<SysMenu> list = sysMenuService.selectList(id);
        List<SysMenu> trees = toTree(list,0);
        return DataResult.success(trees);
    }

    @GetMapping("get/menu/role")
    public DataResult queryMenuFormRole() {
        List<Role> roleMenus = sysMenuService.queryMenuFormRole();
        return DataResult.success(roleMenus);
    }

    private  List<SysMenu> toTree(List<SysMenu> menus, int rootFlag) {
        List<SysMenu> rootSysMenu = menus.stream()
                .filter(it -> it.getParentId() == rootFlag)
                .collect(Collectors.toList());

        List<SysMenu> res = rootSysMenu.stream().map(it -> {
            it.setChild(getChildren(menus, it));
            return it;
        }).collect(Collectors.toList());
        return res;
    }

    private List<SysMenu> getChildren(List<SysMenu> menus, SysMenu sysMenu) {
        List<SysMenu> children = menus.stream()
                .filter(it -> it.getParentId().equals(sysMenu.getId()))
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

