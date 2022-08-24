package com.frame.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.frame.business.entity.User;
import com.frame.business.mapper.UserMapper;
import com.frame.business.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.frame.exception.BusinessException;
import com.frame.exception.code.BaseResponseCode;
import com.frame.vo.UserQueryVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author 邹坤
 * @since 2022-03-12
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User insert(User user) {
        baseMapper.insert(user);
        // 插入后立即获取（集群问题）
        return queryUserByTel(user.getUserMobile());
    }

    @Override
    public User queryUserByTel(String phone) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_mobile", phone);
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     *  查询所有用户 [慎用，没有分页处理] 推荐 {@link UserService:queryAllUsersByPage}
     * @return
     */
    @Override
    public List<User> queryAllUsers() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1);
        return baseMapper.selectList(wrapper);
    }


    /**
     * 用户数据 分页查询
     * @param page
     * @param  name 用户名
     * @return
     */
    @Override
    public Page<User> queryAllUsersByPage(String name, Page<User> page) {

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.like(!StringUtils.isEmpty(name), "nick_name", name);
        wrapper.eq("status", 1);
        Page<User> userPage = baseMapper.selectPage(page, wrapper);
        return userPage;
    }

    @Override
    public List<User> queryByName(String userName) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1)
                .like(!StringUtils.isEmpty(userName), "nick_name", userName);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public Page<User> queryUserByPage(Page<User> objectPage, UserQueryVo userQueryVo) {
        String nickName = userQueryVo.getNickName();
        Integer status = userQueryVo.getStatus();
        String sex = userQueryVo.getSex();
        String userMail = userQueryVo.getUserMail();

        // http://127.0.0.1:8080/user/page?page=1&limit=10
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.like(!StringUtils.isEmpty(nickName), "nick_name", nickName)
                .eq(status!=null,"status", status)
                .eq(!StringUtils.isEmpty(sex), "sex", sex)
                .eq(!StringUtils.isEmpty(userMail), "user_mail", userMail);

        return baseMapper.selectPage(objectPage,wrapper);
    }

    @Override
    public void lockUserById(String id) {
        User user = new User();
        user.setUserId(id);
        user.setStatus(0);
        baseMapper.updateById(user);
    }

    @Override
    public void unlockUserById(String id) {
        User user = new User();
        user.setUserId(id);
        user.setStatus(1);
        baseMapper.updateById(user);
    }


}
