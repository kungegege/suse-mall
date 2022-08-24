package com.frame.business.controller;

import com.alipay.api.AlipayApiException;
import com.frame.business.base.BaseController;
import com.frame.business.entity.Order;
import com.frame.business.service.OrderService;
import com.frame.business.service.PayService;
import com.frame.business.service.ProdService;
import com.frame.utils.DataResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/5/3
 * Time: 22:53
 * Description: 支付模块
 */
@Controller
@Slf4j
@RequestMapping("/pay")
public class PayController extends BaseController {

    @Autowired
    private PayService payService;

    @Autowired
    private OrderService orderService;




    @GetMapping("order/{id}")
    @ResponseBody
    public DataResult pay(@PathVariable String id) throws AlipayApiException, IOException {
        String body = payService.orderPay(id);
        return DataResult.success(body);
    }

    @PostMapping("success/back")
    @ResponseBody
    public void callBack() throws IOException, ServletException {
        HttpServletRequest request = getRequest();
        String orderId = request.getParameter("out_trade_no");
        payService.payCallBack(Long.parseLong(orderId));
        getResponse().getWriter().write("success");
    }

    @RequestMapping("redirect/success")
    public String redirectSuccessPage() {
        return "redirect:http://127.0.0.1:8081";
    }

}
