package com.frame.vo;
import com.frame.business.entity.User;
import lombok.Data;
/**
 * Created with IntelliJ IDEA.
 * @author: HP-zouKun
 * Date: 2022/3/13
 * Time: 17:40
 * Description:
 */
@Data
public class TokenRespVo {

    private String token;

    private UserInfo info;

    public TokenRespVo(String token, User user) {
        this.token = token;
        UserInfo userInfo = new UserInfo();
        userInfo.setId(user.getUserId());
        userInfo.setName(user.getNickName());
        userInfo.setScore(user.getScore());
        userInfo.setPic(user.getPic());
        this.info = userInfo;
    }

    @Data
    class UserInfo {
        private String id;
        private String pic;
        private Integer score;
        private String name;
        private String sex;
    }
}
