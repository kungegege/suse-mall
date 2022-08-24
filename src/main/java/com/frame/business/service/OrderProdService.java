package com.frame.business.service;

import com.frame.business.entity.OrderProd;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 订单商品表 服务类
 * </p>
 *
 * @author 邹坤
 * @since 2022-05-02
 */
public interface OrderProdService extends IService<OrderProd> {

    List<OrderProd> selectByIds(List<Long> orderProdIds);

    List<OrderProd> queryByOrderId(Long id);
}
