package com.frame.business.controller;


import cn.hutool.core.lang.tree.Tree;
import com.alipay.api.domain.Car;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.frame.annotation.RequiresRoles;
import com.frame.business.base.BaseController;
import com.frame.business.entity.*;
import com.frame.business.service.*;
import com.frame.exception.BusinessException;
import com.frame.utils.DataResult;
import com.frame.vo.OrderProdVo;
import com.frame.vo.OrderQueryVo;
import com.frame.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/5/3
 * Time: 22:53
 * Description: 订单模块
 */
@RestController
@RequestMapping("/order")
public class OrderController extends BaseController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProdService prodService;

    /**
     * 生成商品的订单（购物车方式）
     * @return
     */
    @RequiresRoles()
    @GetMapping("create/cart")
    public DataResult createOrderFromCart() {
        OrderVo orderVos = new OrderVo();
        // TODO 待优化
        List<Cart> carts = cartService.queryCart(getCurrentUserId());
        // TODO 并发，商品状态
        List<OrderProdVo> orderProdVos = carts.stream()
                // 获取选中的商品
                .filter(cart -> cart.getStatus() == 1)
                .map(cart -> {
                    Prod prod = prodService.getById(cart.getProdId());
                    if (prod.getTotalStocks() < 1 && prod.getStatus() != 1) {
                        // 商品 缺货 ~
                        throw new BusinessException(90002, prod.getProdName() + "已经售空");
                    }
                    return new OrderProdVo(prod.getProdId(), cart.getNumber());
                }).collect(Collectors.toList());

        orderVos.setProdVos(orderProdVos);

        Order order = orderService.createOrderFromCart(orderVos);
        return DataResult.success(order);
    }

    /**
     * 创建商品的订单（购物车方式）
     * @return
     */
    @RequiresRoles
    @GetMapping("create/prod/{pid}")
    public DataResult createOrder(@PathVariable Long pid) {
        OrderVo orderVo = new OrderVo();
        OrderProdVo orderProdVo = new OrderProdVo(pid, 1);
        ArrayList<OrderProdVo> orderProdVos = new ArrayList<>();
        orderProdVos.add(orderProdVo);
        orderVo.setProdVos(orderProdVos);
        Order order = orderService.createOrderFromCart(orderVo);
        return DataResult.success(order);
    }

    /**
     * 通过ID查询订单数据
     * @param id
     * @return
     */
    @RequiresRoles
    @GetMapping("/query/{id}")
    public DataResult queryOrderById(@PathVariable Long id) {
        Order order = orderService.queryById(id);
        return DataResult.success(order);
    }

    /**
     * 分页查询订单数据
     * @param page
     * @param limit
     * @return
     */
    @GetMapping("/page")
    public DataResult getOrderByPage(Integer page, Integer limit, OrderQueryVo orderQueryVo) {
        if (getCurrentRoles().contains(UserRole.MANAGE_ADMIN.getRoleName())) {
            orderQueryVo.setUserId(null);
        }else{
            orderQueryVo.setUserId(getCurrentUserId());
        }
        Page<Order> orderPages = orderService.selectOrderByPage(new Page<Order>(page, limit), orderQueryVo);
        return DataResult.success(orderPages);
    }

    @GetMapping("/query/user/{uid}")
    public DataResult queryByUserId(@PathVariable String uid) {
        List<Order> orderList = orderService.queryByUserId(uid);
        return DataResult.success(orderList);
    }


    @RequiresRoles
    @GetMapping("query")
    public DataResult query() {
        List<Order> res = orderService.queryByUserId(getCurrentUserId());
        Collections.sort(res);
        return DataResult.success(res);
    }

    /**
     * 修改订单的 收货地址
     * @param orderAddrId
     * @param userAddrId
     * @return
     */
    @RequiresRoles
    @GetMapping("/update/addr/{orderAddrId}/{userAddrId}")
    public DataResult updateOrderAddress(@PathVariable Long orderAddrId, @PathVariable Long userAddrId) {
        OrderAddr orderAddr = orderService.updateOrderAddress(orderAddrId, userAddrId);
        return DataResult.success(orderAddr);
    }


    /**
     * 设置订单的 收货地址
     * @param orderId
     * @param userAddrId
     * @return
     */
    @RequiresRoles
    @GetMapping("/set/addr/{orderId}/{userAddrId}")
    public DataResult updateOrderAddressByOrderId(@PathVariable Long orderId, @PathVariable Long userAddrId) {
        UserAddr userAddr = orderService.updateOrderAddressByOrderId(orderId, userAddrId);
        return DataResult.success(userAddr);
    }

    /**
     * 取消订单
     * @param orderId
     * @return
     */
    @GetMapping("/cancel/{orderId}")
    public DataResult cancelOrder(@PathVariable Long orderId) {
        orderService.removeById(orderId);
        return DataResult.success();
    }

    /**
     * 查询订单中的商品数据
     * @param oid
     * @return
     */
    @GetMapping("prod/{oid}")
    public DataResult queryProdFormOrder(@PathVariable Long oid) {
        List<OrderProd> orderProds = orderService.queryProdFormOrder(oid);
        return DataResult.success(orderProds);
    }


    @GetMapping("/update/{orderId}/status/{status}")
    public DataResult updateOrderStatus(@PathVariable Long orderId, @PathVariable Integer status) {
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(status);
        orderService.updateById(order);
        return DataResult.success();
    }

}

