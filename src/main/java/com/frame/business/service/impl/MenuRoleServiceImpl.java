package com.frame.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.frame.business.entity.MenuRole;
import com.frame.business.entity.SysMenu;
import com.frame.business.mapper.MenuRoleMapper;
import com.frame.business.service.MenuRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 * @author 邹坤
 * @since 2022-04-13
 */
@Service
public class MenuRoleServiceImpl extends ServiceImpl<MenuRoleMapper, MenuRole> implements MenuRoleService {

    @Override
    public List<MenuRole> selectByRolesId(List<Integer> roleIds) {
        if (roleIds==null || roleIds.isEmpty()) {
            return Collections.emptyList();
        }
        QueryWrapper<MenuRole> wrapper = new QueryWrapper<>();
        wrapper.in("role_id", roleIds);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public List<MenuRole> selectByRoleId(Integer roleId) {
        QueryWrapper<MenuRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId);
        return baseMapper.selectList(queryWrapper);
    }

}
