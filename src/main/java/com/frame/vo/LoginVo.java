package com.frame.vo;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/3/13
 * Time: 16:01
 * Description:
 */
@Data
public class LoginVo {
    private String mobile;
    private String password;
    private String code;
    /**
     * 是否是后台登录
     */
    private Boolean isSystemLogin;
}
