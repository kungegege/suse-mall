package com.frame.config;

import com.frame.business.service.ChatService;
import com.frame.business.service.UserService;
import com.frame.business.service.WebSocket;
import com.frame.utils.RedisTemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * Created with IntelliJ IDEA.
 * @author: HP-zouKun
 * Date: 2022/3/3
 * Time: 16:31
 * Description:
 */
@Configuration
public class WebSocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Autowired
    public void setSenderService(RedisTemplateUtil redisTemplateUtil,
                                 ChatService chatService,
                                 UserService userService) {
        WebSocket.redisTemplateUtil = redisTemplateUtil;
        WebSocket.chatService = chatService;
        WebSocket.userService = userService;
    }
}
