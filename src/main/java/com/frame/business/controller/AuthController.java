package com.frame.business.controller;
import com.frame.annotation.RequiresRoles;
import com.frame.business.base.BaseController;
import com.frame.business.entity.Role;
import com.frame.business.entity.User;
import com.frame.business.service.RoleService;
import com.frame.business.service.UserService;
import com.frame.utils.DataResult;
import com.frame.vo.TokenRespVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * @author: HP-zouKun
 * Date: 2022/5/1
 * Time: 20:42
 * Description:
 */
@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    /**
     * 令牌，自动续期
     * @return
     */
    @GetMapping(value = {"/check" })
    @RequiresRoles
    public DataResult checkAndExtendToken() {
        String token = getToken();

        boolean isExtend = jwtService.isExtendExpireTime(token);
        if (isExtend) {
            // 令牌，自动续期
            String userId = getCurrentUserId();
            User user = userService.getById(userId);
            List<Role> roles = roleService.getByUserId(userId);
            log.info("用户：ID->{},username->{}, 续期了令牌", user.getUserId(), user.getNickName());
            // 设置Cookie
            setToken(jwtService.genToken(user, roles));

            return DataResult.success(new TokenRespVo(token, user));
        }

        return DataResult.success();
    }

}
