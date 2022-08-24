package com.frame.business.service.impl;

import com.frame.business.entity.OrderAddr;
import com.frame.business.entity.UserAddr;
import com.frame.business.mapper.OrderAddrMapper;
import com.frame.business.service.OrderAddrService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 用户配送地址 服务实现类
 * </p>
 *
 * @author 邹坤
 * @since 2022-05-08
 */
@Service
public class OrderAddrServiceImpl extends ServiceImpl<OrderAddrMapper, OrderAddr> implements OrderAddrService {

    @Override
    public OrderAddr copyAndSave(UserAddr userAddr) {
        OrderAddr orderAddr = new OrderAddr();
        orderAddr.setAddr(userAddr.getAddr());
        orderAddr.setCreateTime(LocalDateTime.now());
        orderAddr.setMobile(userAddr.getMobile());
        orderAddr.setReceiver(userAddr.getReceiver());
        orderAddr.setRegion(userAddr.getRegion());
        orderAddr.setUserId(userAddr.getUserId());
        orderAddr.setUserAddrId(userAddr.getId());
        baseMapper.insert(orderAddr);
        return orderAddr;
    }


}
