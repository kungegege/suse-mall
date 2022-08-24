package com.frame.business.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/5/4
 * Time: 21:27
 * Description:
 */
@Controller
public class IndexController {
    @GetMapping("/success")
    public String paySuccessPage() {
        // 前端 支付成功 页面
        return "redirect:http://127.0.0.1:8081/pages/money/paySuccess";
    }
}
