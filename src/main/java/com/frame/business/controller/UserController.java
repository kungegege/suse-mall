package com.frame.business.controller;


import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.frame.annotation.RequiresRoles;
import com.frame.business.base.BaseController;
import com.frame.business.entity.Role;
import com.frame.business.entity.User;
import com.frame.business.entity.UserRole;
import com.frame.business.service.*;
import com.frame.contants.Constant;
import com.frame.contants.CookieConstant;
import com.frame.exception.BusinessException;
import com.frame.exception.code.BaseResponseCode;
import com.frame.utils.*;
import com.frame.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * TODO 密码过敏处理
 * @author 邹坤
 * @since 2022-03-12
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private SmsService smsService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private CaptchaController captchaController;


    @PostMapping("/login")
    public DataResult login(@RequestBody  LoginVo loginVo) {
        log.info("{}, 用户登录：{}", getIp(), loginVo);

        if ( loginVo.getIsSystemLogin()) {
            // 验证码校验
            Boolean res = captchaController.captchaValid(loginVo.getCode());
            if (!res) {
                return DataResult.getResult(BaseResponseCode.CODE_ERROR);
            }
        }
        User user = userService.queryUserByTel( loginVo.getMobile() );

        if (user == null) {
            throw new BusinessException(BaseResponseCode.ACCOUNT_ERROR);
        }

        // 用户状态判断
        if (user.getStatus() == 0) {
            throw new BusinessException(BaseResponseCode.ACCOUNT_LOCK_TIP);
        }
        // 密码校验
        if (!PasswordUtil.matches(loginVo.getPassword(), user.getLoginPassword())) {
            throw new BusinessException(BaseResponseCode.ACCOUNT_PASSWORD_ERROR);

        }
        // 签发令牌
        String token = generateToken(user);

        // 设置Cookie
        setToken(token);
        return DataResult.success(new TokenRespVo(token, user));
    }


    @PostMapping("/register")
    public DataResult register(@RequestBody RegisterVo registerVo) {
        log.info("用户注册：{}", registerVo);

        // 验证码校验
        if (!validCode(registerVo.getPhone(), registerVo.getCode())) {
            throw new BusinessException(BaseResponseCode.CODE_ERROR);
        }

        // 该手机号是否被注册
        if (userService.queryUserByTel(registerVo.getPhone())!=null) {
            throw new BusinessException(BaseResponseCode.PHONE_HAS_REGISTER);
        }

        User user = new User();
        // TODO 密码加密
        user.setLoginPassword(PasswordUtil.encode(registerVo.getPassword()));
        user.setUserMobile(registerVo.getPhone());

        user.setModifyTime(LocalDateTime.now());
        user.setUserRegtime(LocalDateTime.now());
        user.setUserRegip(IpUtils.getIpAddr(getRequest()));
        user.setUserLastip(IpUtils.getIpAddr(getRequest()));
        // 随机用户名
        user.setNickName("游客-" + UUID.randomUUID().toString().substring(0, 4));
        user = userService.insert(user);

        // TODO 线程池执行 用户角色绑定（拿到用户ID后）
        roleService.insert(user.getUserId(), UserRole.NORMAL_USER.getRoleId());

        return DataResult.success();
    }


    @GetMapping("/loginout")
    public DataResult loginout() {
        Cookie cookie = new Cookie(CookieConstant.USER_TOKEN.getName(), "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        HttpServletResponse response = getResponse();
        response.addCookie(cookie);
        return DataResult.success();
    }

    // TODO 后端管理权限
    @GetMapping("/all")
    // @RequiresRoles("admin")
    public DataResult getUsers(Integer currPage, Integer size, String name) {
        Page<User> userPage = userService.queryAllUsersByPage(name, new Page<User>(currPage, size));
        return DataResult.success(userPage);
    }
    /**
     * 验证码校验
     * @param key  绑定的key
     * @param code 验证码的值
     */
    private boolean validCode(String key, String code) {
        if (StringUtils.isEmpty(key)) {   return false;  }
        Object targetCode = redisTemplateUtil.get(Constant.CODE_PREFIX + key);
        if (key == null || !code.equalsIgnoreCase((String) targetCode)) {
            return false;
        }
        // 删除验证码
        redisTemplateUtil.delete(Constant.CODE_PREFIX + key);
        return true;
    }

    /**
     * 获取短信
     * @param phoneNumber
     * @return
     */
    @GetMapping("/sms/{phoneNumber}")
    public DataResult getMsgCode(@PathVariable String phoneNumber) {
        String code = RandomUtil.randomNumbers(6);
        if (smsService.sendMsgCode(phoneNumber, code)) {
            log.info("发送短信至：{}， code: {}", phoneNumber, code);
             redisTemplateUtil.set(Constant.CODE_PREFIX + phoneNumber, code, 5, TimeUnit.MINUTES);
            return DataResult.success();
        }
        return DataResult.getResult(BaseResponseCode.SYSTEM_ERROR);
    }

    @GetMapping("/page")
    public DataResult getProductions(Integer page, Integer limit, UserQueryVo userQueryVo) {
        Page<User> users = userService.queryUserByPage(new Page<>(page, limit), userQueryVo);
        return DataResult.success(users);
    }

    @GetMapping("/lock/{id}")
    public DataResult lockUserById(@PathVariable String id) {
        userService.lockUserById(id);
        return DataResult.success();
    }


    @GetMapping("/unlock/{id}")
    public DataResult unlockUserById(@PathVariable String id) {
        userService.unlockUserById(id);
        return DataResult.success();
    }

    // TODO 屏蔽敏感数据
    @GetMapping("/{id}")
    public DataResult getUserInfo(@PathVariable String id) {
        User user = userService.getById(id);
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        userVo.setId(user.getUserId());
        return DataResult.success(userVo);
    }

    @RequiresRoles
    @GetMapping("info")
    public DataResult getUserInfo() {
        return this.getUserInfo(getCurrentUserId());
    }

    @GetMapping("query")
    @RequiresRoles
    public DataResult queryUserInfo() {
        return getUserInfo(getCurrentUserId());
    }

    @PostMapping("/update")
    public DataResult updateById(@RequestBody UserVo user) {
        User user1 = new User();
        BeanUtils.copyProperties(user, user1);
        userService.updateById(user1);
        return DataResult.success();
    }

    @PostMapping("/update/pwd")
    public DataResult updatePassword(@RequestBody PasswordUpdateVo passwordUpdateVo) {
        String newPassword = passwordUpdateVo.getNewPassword();
        String confirmPassword = passwordUpdateVo.getConfirmPassword();
        String oldPassword = passwordUpdateVo.getOldPassword();
        String id = passwordUpdateVo.getUserId();

        if (!newPassword.equals(confirmPassword)) {
            throw new BusinessException(BaseResponseCode.NOT_EQUAL_PASSWORD);
        }

        if (StringUtils.isEmpty(id)) {
            id = getCurrentUserId();
        }

        User user = userService.getById(id);

        if (user == null || !user.getLoginPassword().equals(oldPassword)) {
            throw new BusinessException(BaseResponseCode.PASSWORD_ERROR);
        }

        // 修改密码， TODO  待加密
        User passwordUser = new User();
        passwordUser.setUserId(user.getUserId());
        passwordUser.setLoginPassword(newPassword);
        userService.updateById(passwordUser);

        return DataResult.success();
    }


    @GetMapping("/update/pic")
    public  DataResult updatePic(@RequestParam("pic") String pic ){
        String userId = getCurrentUserId();
        User user = new User();
        user.setUserId(userId);
        user.setPic(pic);
        userService.updateById(user);
        return DataResult.success();
    }

    /**
     * 生成token 凭证
     * @param user
     * @return
     */
    private String generateToken(User user){
        List<Role> roles = roleService.getByUserId(user.getUserId());
        String token = jwtService.genToken(user, roles);
        return token;
    }
}

