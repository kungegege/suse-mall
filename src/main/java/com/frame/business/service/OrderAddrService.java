package com.frame.business.service;

import com.frame.business.entity.OrderAddr;
import com.baomidou.mybatisplus.extension.service.IService;
import com.frame.business.entity.UserAddr;

/**
 * <p>
 * 用户配送地址 服务类
 * </p>
 *
 * @author 邹坤
 * @since 2022-05-08
 */
public interface OrderAddrService extends IService<OrderAddr> {

    OrderAddr copyAndSave(UserAddr userAddr);

}
