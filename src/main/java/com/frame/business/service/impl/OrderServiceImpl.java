package com.frame.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.frame.business.base.BaseController;
import com.frame.business.entity.*;
import com.frame.business.mapper.OrderMapper;
import com.frame.business.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.frame.vo.OrderProdVo;
import com.frame.vo.OrderQueryVo;
import com.frame.vo.OrderVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 邹坤
 * @since 2022-05-02
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private ProdService prodService;

    @Autowired
    BaseController baseController;

    /**
     * 中间表 service
     */
    @Autowired
    private ProdOrderCorrService prodOrderCorrService;

    @Autowired
    private OrderProdService orderProdService;

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderAddrService orderAddrService;

    @Autowired
    private UserAddrService userAddrService;

    /**
     * 初始化订单 （待付款-状态）
     * @param orderVos
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order createOrderFromCart(OrderVo orderVos) {
        // TODO 商品数量限制（并发），超卖问题
        Order orderEntity = new Order();
        // 1. 保存默认收货地址
        UserAddr userAddr = userAddrService.queryDefaultAddr(baseController.getCurrentUserId());
        if (userAddr != null) {
            OrderAddr orderAddr = orderAddrService.copyAndSave(userAddr);
            orderEntity.setOrderAddr(orderAddr);
            orderEntity.setOrderAddrId(userAddr.getId());
        } else {
            // 没有默认地址
            orderEntity.setOrderAddr(null);
            orderEntity.setOrderAddrId(null);
        }
        //2. 创建订单 -> order表
        orderEntity.setCreateTime(LocalDateTime.now());
        orderEntity.setUserId(baseController.getCurrentUserId());
        orderEntity.setStatus(orderEntity.getStatus());
        orderEntity.setRemake(orderVos.getRemake());
        //  设置为待付款状态
        orderEntity.setStatus(Order.OrderStatus.NO_PAY);
        orderEntity.setPayAmount(culTotalPrice(orderVos));
        List<OrderProdVo> prodVos = orderVos.getProdVos();
        baseMapper.insert(orderEntity);

        prodVos.forEach(it -> {
            //3. 添加订单商品记录 ---> orderProd表
            Prod prod = prodService.getById(it.getProdId());
            OrderProd orderProd = new OrderProd();
            BeanUtils.copyProperties(prod, orderProd);
            orderProd.setNumber(it.getProdNum());
            orderProdService.save(orderProd);

            //4. 创建中间表对应关系 --->ProdOrderCorr表
            ProdOrderCorr prodOrderCorr = new ProdOrderCorr();
            prodOrderCorr.setOrderProdId(orderProd.getId());
            prodOrderCorr.setOrderId(orderEntity.getId());
            prodOrderCorrService.save(prodOrderCorr);
        });

        //5. 清空购物车商品
        cartService.clearCartProd(baseController.getCurrentUserId(), Cart.ACTIVE_STATUS);
        return orderEntity;
    }

    @Override
    public Page<Order> selectOrderByPage(Page<Order> orderPage, OrderQueryVo orderQueryVo) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(orderQueryVo.getUserId() != null, "user_id", orderQueryVo.getUserId());
        Page<Order> page = baseMapper.selectPage(orderPage, queryWrapper);
        List<Order> orders = page.getRecords();
        // TODO 待：SQL 来联合查询优化
        for (Order order : orders) {
            // 查询订单商品信息
            List<ProdOrderCorr> prodOrderCorrs = prodOrderCorrService.selectByOrderId(order.getId());
            List<Long> orderProdIds = prodOrderCorrs.stream().map(it -> it.getOrderProdId()).collect(Collectors.toList());
            order.setOrderProds(orderProdService.selectByIds(orderProdIds));

            // 查询收货人详信息
            User user = userService.getById(order.getUserId());
            order.setCreateUser(user);

            // 查询收货地址
            OrderAddr orderAddr = orderAddrService.getById(order.getOrderAddrId());
            order.setOrderAddr(orderAddr);
        }
        return page;
    }

    @Override
    public List<Order> queryByUserId(String uid) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", uid);
        List<Order> orders = baseMapper.selectList(queryWrapper);
        User user = userService.getById(uid);
        for (Order order : orders) {
            order.setCreateUser(user);
            order.setOrderProds(getOrderProdByOrderId(order.getId()));
            // 查询收货地址
            OrderAddr orderAddr = orderAddrService.getById(order.getOrderAddrId());
            order.setOrderAddr(orderAddr);
        }
        return orders;
    }

    @Override
    public List<OrderProd> getOrderProd(Long id) {
        List<Long> orderProdIds = prodOrderCorrService.selectByOrderId(id).stream().map(it -> it.getOrderProdId()).collect(Collectors.toList());
        return orderProdService.selectByIds(orderProdIds);
    }

    @Override
    public Order queryById(Long id) {
        Order order = this.getById(id);
        // 查询收货地址
        OrderAddr orderAddr = orderAddrService.getById(order.getOrderAddrId());
        order.setOrderAddr(orderAddr);
        order.setOrderProds(getOrderProdByOrderId(order.getId()));
        return order;
    }

    @Override
    public OrderAddr updateOrderAddress(Long orderAddrId, Long userAddrId) {
        UserAddr userAddr = userAddrService.getById(userAddrId);
        OrderAddr orderAddr = new OrderAddr();
        BeanUtils.copyProperties(userAddr, orderAddr);

        orderAddr.setUserAddrId(userAddrId);
        orderAddr.setId(orderAddrId);

        orderAddr.setUpdateTime(LocalDateTime.now());
        orderAddrService.updateById(orderAddr);
        return orderAddr;
    }

    @Override
    public List<OrderProd> queryProdFormOrder(Long oid) {
        return getOrderProdByOrderId(oid);

    }

    @Override
    public UserAddr updateOrderAddressByOrderId(Long orderId, Long userAddrId) {
        UpdateWrapper<Order> orderUpdateWrapper = new UpdateWrapper<>();
        orderUpdateWrapper.set("order_addr_id", userAddrId)
                .eq("id", orderId);
        baseMapper.update(null, orderUpdateWrapper);
        UserAddr addr = userAddrService.getById(userAddrId);
        return addr;
    }

    // TODO 生成订单后，待付款（但商品价格修改了?）
    private BigDecimal culTotalPrice(OrderVo orderVos) {
        BigDecimal totalPrice = new BigDecimal(0);
        for (OrderProdVo prodVo : orderVos.getProdVos()) {
            Prod prod = prodService.getById(prodVo.getProdId());
            // TODO 运费
            BigDecimal mul = prod.getPrice().multiply(new BigDecimal(prodVo.getProdNum()));
            totalPrice = totalPrice.add(mul);
        }
        return totalPrice;
    }

    /**
     * 根据订单ID， 查询订单中的商品列表
     * @param id
     * @return
     */
    private List<OrderProd> getOrderProdByOrderId(Long id) {
        List<ProdOrderCorr> prodOrderCorrs = prodOrderCorrService.selectByOrderId(id);
        List<OrderProd> orderCorrArrayList = new ArrayList<>();
        for (ProdOrderCorr prodOrderCorr : prodOrderCorrs) {
            Long orderProdId = prodOrderCorr.getOrderProdId();
            OrderProd orderProd = orderProdService.getById(orderProdId);
            orderCorrArrayList.add(orderProd);
        }
        return orderCorrArrayList;
    }

}
