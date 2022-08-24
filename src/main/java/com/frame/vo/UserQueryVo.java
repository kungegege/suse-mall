package com.frame.vo;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/4/1
 * Time: 12:25
 * Description:
 */
@Data
public class UserQueryVo {
    private String userId;
    private String nickName;
    private String userMail;
    private String sex;
    private Integer status;
}
