package com.frame.business.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.frame.business.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.frame.business.entity.OrderAddr;
import com.frame.business.entity.OrderProd;
import com.frame.business.entity.UserAddr;
import com.frame.vo.OrderQueryVo;
import com.frame.vo.OrderVo;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 邹坤
 * @since 2022-05-02
 */
public interface OrderService extends IService<Order> {

    Order createOrderFromCart(OrderVo  orderVos);

    /**
     * 订单分页查询
     * @param orderPage
     * @param orderQueryVo
     * @return
     */
    Page<Order> selectOrderByPage(Page<Order> orderPage, OrderQueryVo orderQueryVo);

    List<Order> queryByUserId(String uid);

    List<OrderProd> getOrderProd(Long id);

    Order queryById(Long id);

    /**
     * 修改订单的 收货地址
     * @param orderId
     * @param userAddrId
     */
    OrderAddr updateOrderAddress(Long orderId, Long userAddrId);

    /**
     * 查询指定订单的商品数据
     * @param oid
     * @return
     */
    List<OrderProd> queryProdFormOrder(Long oid);

    UserAddr updateOrderAddressByOrderId(Long orderId, Long userAddrId);
}
