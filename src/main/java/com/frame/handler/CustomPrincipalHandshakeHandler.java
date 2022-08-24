package com.frame.handler;

import lombok.Data;

import java.security.Principal;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/6/8
 * Time: 17:48
 * Description: 用于保存websocket连接过程中需要存储的业务参数
 */
@Data
public class CustomPrincipalHandshakeHandler implements Principal {

    private String token;

    public CustomPrincipalHandshakeHandler(String token) {
        this.token = token;
    }

    @Override
    public String getName() {
        return token;
    }

}
