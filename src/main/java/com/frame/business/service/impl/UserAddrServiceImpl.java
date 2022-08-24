package com.frame.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.frame.business.entity.UserAddr;
import com.frame.business.mapper.UserAddrMapper;
import com.frame.business.service.UserAddrService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 用户配送地址 服务实现类
 * </p>
 *
 * @author 邹坤
 * @since 2022-04-30
 */
@Service
public class UserAddrServiceImpl extends ServiceImpl<UserAddrMapper, UserAddr> implements UserAddrService {

    @Override
    public void addUserAddress(UserAddr userAddr) {
        baseMapper.insert(userAddr);
    }

    @Override
    public List<UserAddr> queryByUId(String userId) {
        QueryWrapper<UserAddr> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public UserAddr queryDefaultAddr(String userId) {
        QueryWrapper<UserAddr> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId)
                .eq("is_default", 1);
        List<UserAddr> userAddrs = baseMapper.selectList(wrapper);
        if (userAddrs.isEmpty()) {
            return null;
        }
        return userAddrs.get(0);
    }

    @Override
    public void resetDefaultAddress() {
        UpdateWrapper<UserAddr> wrapper = new UpdateWrapper<>();
        wrapper.set("is_default", 2);
        baseMapper.update(null, wrapper);
    }
}
