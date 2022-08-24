package com.frame.vo;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/4/15
 * Time: 17:04
 * Description:
 */
@Data
public class PasswordUpdateVo {

    private String userId;

    private String oldPassword;

    private String newPassword;

    private String confirmPassword;

}
