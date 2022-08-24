package com.frame.business.controller;

import cn.hutool.captcha.LineCaptcha;
import com.frame.business.base.BaseController;
import com.frame.business.service.CaptchaService;
import com.frame.utils.DataResult;
import com.frame.utils.RedisTemplateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/4/11
 * Time: 15:29
 * Description:
 */
@RestController
@Slf4j
public class CaptchaController extends BaseController {

    @Autowired
    private CaptchaService captchaService;

    @Value("${captcha.validTime}")
    private Integer validTime;

    private static final String prefix = "captcha";

    @GetMapping("/captcha/get")
    public DataResult<String> getCaptcha() {
        LineCaptcha captcha = captchaService.GenerateCaptcha();
        String code = captcha.getCode();
        redisTemplateUtil.set(prefix + getIp(), code, validTime, TimeUnit.MINUTES);
        String imageBase64 = captcha.getImageBase64Data();
        return DataResult.success(imageBase64);
    }

    public Boolean captchaValid(String code) {
        String redisCode = (String)redisTemplateUtil.get(prefix + getIp());
        log.info("redis 中的验证码：{} , 目标验证码为{}", redisCode, code);
        if (redisCode == null || !redisCode.equalsIgnoreCase(code)) {
            return false;
        }
        redisTemplateUtil.delete(prefix + getIp());
        return true;
    }

}
