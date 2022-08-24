package com.frame.vo;

import com.frame.business.entity.User;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * @author: HP-zouKun
 * Date: 2022/4/15
 * Time: 16:02
 * Description:
 */
@Data
public class UserVo {
    private String id;
    private String nickName;
    private String userId;
    private String userMail;
//    private String userMobile;
    private String sex;
    private String pic;
    private Integer score;
    private String userMemo;

    public static UserVo of(User user) {
        UserVo userVo = new UserVo();
        userVo.setUserId(user.getUserId());
        userVo.setUserMail(user.getUserMail());
        userVo.setUserMemo(user.getUserMemo());
        userVo.setPic(user.getPic());
        userVo.setSex(user.getSex());
        userVo.setNickName(user.getNickName());
        userVo.setScore(user.getScore());
        return userVo;
    }

}
