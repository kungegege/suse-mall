package com.frame.business.controller;

import com.sun.org.apache.regexp.internal.RE;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/3/3
 * Time: 15:36
 * Description: 测试接口
 */

@Controller
public class ChartController {

    @GetMapping("/chatroom")
    public String page() {
        return "chat";
    }

    @GetMapping("/host")
    public String hostPage() {
        return "host";
    }

}
