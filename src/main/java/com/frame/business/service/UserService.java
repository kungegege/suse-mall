package com.frame.business.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.frame.business.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.frame.vo.UserQueryVo;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author 邹坤
 * @since 2022-03-12
 */
public interface UserService extends IService<User> {

    User insert(User user);

    User queryUserByTel(String phone);

    List<User> queryAllUsers();

    Page<User> queryAllUsersByPage(String name, Page<User> page);

    List<User> queryByName(String userName);

    Page<User> queryUserByPage(Page<User> objectPage, UserQueryVo userQueryVo);

    void lockUserById(String id);

    void unlockUserById(String id);
}
