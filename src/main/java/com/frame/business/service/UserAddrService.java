package com.frame.business.service;

import com.frame.business.entity.UserAddr;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户配送地址 服务类
 * </p>
 *
 * @author 邹坤
 * @since 2022-04-30
 */
public interface UserAddrService extends IService<UserAddr> {

    void addUserAddress(UserAddr userAddr);

    List<UserAddr> queryByUId(String userId);

    UserAddr queryDefaultAddr(String userId);

    void resetDefaultAddress();

}
