package com.frame.business.base;
import cn.hutool.json.JSONObject;
import com.frame.business.entity.Role;
import com.frame.business.entity.User;
import com.frame.business.service.JwtService;
import com.frame.business.service.UserService;
import com.frame.contants.Constant;
import com.frame.contants.CookieConstant;
import com.frame.utils.IpUtils;
import com.frame.utils.RedisTemplateUtil;
import com.frame.utils.ServletUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * Created with IntelliJ IDEA.
 * @author: HP-zouKun
 * Date: 2022/3/13
 * Time: 14:13
 * Description:
 */

@Component
public  class BaseController {

    @Autowired
    protected JwtService jwtService;

    @Autowired
    protected RedisTemplateUtil redisTemplateUtil;

    public HttpServletRequest getRequest() {
        return ServletUtils.getRequest();
    }

    public HttpServletResponse getResponse(){
        return ServletUtils.getResponse();
    }

    public String getCookie(CookieConstant name) {
        HttpServletRequest request = getRequest();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public String getToken(){
        // 兼容没有Cookie的平台（小程序）， 优先获取请求头中的authorization属性
        String authorization = getRequest().getHeader("authorization");
        if (!StringUtils.isEmpty(authorization)) {
            return authorization;
        }
        return getCookie(CookieConstant.USER_TOKEN);
    }

    /**
     * 获取当前用户的Id
     * @return
     */
    public String getCurrentUserId(){
        JSONObject jsonObject = jwtService.parse(getToken());
        return (String) jsonObject.get("id");
    }

    /**
     * 获取当前用户的角色信息
     * @return
     */
    public List<String> getCurrentRoles() {
        String token = getToken();
        if (StringUtils.isEmpty(token)) {
            return Collections.emptyList();
        }

        String strRoles = (String) jwtService.parse(token, "roles");

        if (strRoles == null || strRoles.length() == 0) {
            return Collections.emptyList();
        }

        String[] arrRoles = strRoles.split(",");
        List<String> roles = new ArrayList<>();
        for (String role : arrRoles) {
            roles.add(role);
        }
        return roles;
    }

    public String getIp() {
        HttpServletRequest request = getRequest();
        return IpUtils.getIpAddr(request);
    }

    public boolean hasLogin() {
        String token = getToken();
        return jwtService.verify(token);
    }

    /**
     * 设置Cookie
     */
    public void setToken(String token){
        HttpServletResponse response = getResponse();
        Cookie cookie = new Cookie(CookieConstant.USER_TOKEN.getName(), token);
        cookie.setPath("/");

        cookie.setHttpOnly(false);
        cookie.setSecure(false);
        response.addCookie(cookie);
    }

}
