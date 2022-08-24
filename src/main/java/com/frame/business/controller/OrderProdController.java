package com.frame.business.controller;


import com.frame.business.entity.OrderProd;
import com.frame.business.service.OrderProdService;
import com.frame.utils.DataResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 订单商品表 前端控制器
 * </p>
 *
 * @author 邹坤
 * @since 2022-05-02
 */
@RestController
@RequestMapping("/orderProd")
public class OrderProdController {

    @Autowired
    private OrderProdService orderProdService;

    @GetMapping("/order/{id}")
    public DataResult queryByOrderId(@PathVariable Long id) {
        List<OrderProd> res = orderProdService.queryByOrderId(id);
        return DataResult.success(res);
    }



}

