package com.frame.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

/**
 * Created with IntelliJ IDEA.
 * @author: HP-zouKun
 * Date: 2022/6/8
 * Time: 17:59
 * Description: webSocket消息监听，用于监听websocket用户连接情况
 */
@Slf4j
public class WebSocketChannelInterceptor implements ChannelInterceptor {


    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {

    }

    /**
     * 在消息发送之前调用，方法中可以对消息进行修改，如果此方法返回值为空，则不会发生实际的消息发送调用
     * @param message
     * @param channel
     * @return
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        // 判断是否为首次连接请求，如果已经连接过，直接返回message
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            // 这里获取就是JS stompClient.connect(headers, function (frame){.......}) 中header的信息
            // header参数的key可以一样，取出来就是list
            String token = accessor.getNativeHeader("token").get(0);
            CustomPrincipalHandshakeHandler user = (CustomPrincipalHandshakeHandler) accessor.getUser();
            System.out.println("认证用户:" + user.toString() + " 页面传递令牌" + token);
        } else if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
            System.out.println("用户:" + accessor.getUser() + " 断开连接");
        }
        return message;
    }

    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
    }

    @Override
    public void afterReceiveCompletion(Message<?> message, MessageChannel channel, Exception ex) {
    }

    @Override
    public Message<?> postReceive(Message<?> message, MessageChannel channel) {
        return null;
    }

    @Override
    public boolean preReceive(MessageChannel channel) {
        return false;
    }
}
