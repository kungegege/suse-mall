package com.frame.business.controller;

import com.frame.business.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/4/9
 * Time: 17:12
 * Description:
 */
@RequestMapping
@Controller
public class AdminController extends BaseController {

    @Autowired
    private CaptchaController captchaController;

    @GetMapping({"/login", "/login.html"})
    public String adminLogin(Model model) {
        try {
            if (hasLogin()) {
                return "redirect:/index";
            }
        } catch (Exception e) {

        }
        // 未登录，重定向到登录页面
        String base64Img = captchaController.getCaptcha().getData();
        model.addAttribute("captcha", base64Img);

        return "auth/login";
    }

    @GetMapping(value = {"", "/admin", "/admin.html", "/index", "index.html"})
    public String adminPage(Model model) {
        try {
            if (hasLogin()) {
                return "index";
            }
        } catch (Exception e) {

        }
        return "redirect:login";
    }
}
