package com.frame.business.service;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.RandomUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.frame.business.entity.Order;
import com.frame.business.entity.OrderProd;
import com.frame.config.PayConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/5/3
 * Time: 19:29
 * Description:
 */
@Service
@Slf4j
public class PayService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private AlipayClient alipayClient;

    @Autowired
    private ProdService prodService;

    @Autowired
    private PayConfig payConfig;

    public String orderPay(String id) throws AlipayApiException {

        AlipayTradeWapPayRequest alipayTradeWapPayRequest = new AlipayTradeWapPayRequest();
        alipayTradeWapPayRequest.setReturnUrl(payConfig.getReturnUrl());
        alipayTradeWapPayRequest.setNotifyUrl(payConfig.getNotifyUrl());
        AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();

        Order order = orderService.getById(id);
        model.setProductCode("FAST_INSTANT_TRADE_PAY");
        model.setOutTradeNo(order.getId() + "");
        model.setTotalAmount(order.getPayAmount().setScale(2, BigDecimal.ROUND_HALF_UP) + "");

        /* 商品名称 */
        List<OrderProd> orderProds = orderService.getOrderProd(order.getId());
        Object[] subject = orderProds.stream().map(orderProd -> orderProd.getProdName()).toArray();

        String sub = ArrayUtil.join(subject, "，");
        log.info("订单主题，subject: {}", sub);
        model.setSubject(sub);

        alipayTradeWapPayRequest.setBizModel(model);
        log.info(">>>>支付宝统一下单接口请求参数：TradeNo->{},totalAmount->{}", model.getOutTradeNo(), model.getTotalAmount());

        return alipayClient.pageExecute(alipayTradeWapPayRequest).getBody();
    }

    @Transactional(rollbackFor = Exception.class)
    public void payCallBack(long orderId) {
        // 支付成功，修改订单状态,  修改库存
        Order order = orderService.getById(orderId);
        log.info("order ID:{}, 支付成功", order.getId());
        order.setStatus(Order.OrderStatus.NO_RECEIVE);
        // 设置支付方式 1-支付宝
        order.setPayType(1);
        orderService.updateById(order);
        // 更新库存 （库存-- ， 销量++）
        List<OrderProd> orderProds = orderService.getOrderProd(orderId);
        for (OrderProd orderProd : orderProds) {
            Long prodId = orderProd.getProdId();
            prodService.updateStock(-orderProd.getNumber(), prodId);
            prodService.updateSoldNum(orderProd.getNumber(), prodId);
        }

    }
}
