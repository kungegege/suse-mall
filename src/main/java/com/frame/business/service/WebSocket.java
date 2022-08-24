package com.frame.business.service;

/**
 * Created with IntelliJ IDEA.
 * @author: HP-zouKun
 * Date: 2022/3/3
 * Time: 15:13
 * Description:     采用redis，存储在线用户 + 消息历史记录
 *      redis存储结构:  hash + list
 *              1. hash 用于存储 对话用户， key为固定为chat-client-hash， hash<k，v> 中的K为会话发起者ID, v为“发起者ID-目标用户ID”
 *              2. list 存储对话历史消息， key为 “发起者ID-目标用户ID”， value为每条消息记录。
 */

import com.alibaba.fastjson.JSON;
import com.frame.business.entity.Chat;
import com.frame.utils.RedisTemplateUtil;
import com.frame.vo.ChatMessage;
import com.frame.vo.UserVo;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint(value = "/webSocket/{currUserId}/{targetId}")
@Slf4j
@ToString
@Data
public class WebSocket {
    private Session session;
    private String currUserId;
    private String targetClientId;

    public static RedisTemplateUtil redisTemplateUtil;

    public static ChatService chatService;

    public static UserService userService;

    private static ConcurrentHashMap<String, WebSocket> webSocketMap = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(@PathParam("currUserId") String currUserId, @PathParam("targetId") String target, Session session) {
        this.session = session;
        this.currUserId = currUserId;
        this.targetClientId = target;
        log.info("webSocket connect success, {} current to the {} client", webSocketMap.size(), target);
        webSocketMap.put(currUserId, this);
    }

    /**
     * 收到的来自客户端的消息 msg
     * @param msg 发送的消息
     */
    @OnMessage
    public void onMessage(String msg, Session session) throws IOException {
        // 分装消息
        ChatMessage chatMessage = new ChatMessage(msg, this.targetClientId, this.currUserId);
        // 入库
        Chat chat = chatService.saveChatMessage(chatMessage);

        String chatJson = JSON.toJSONString(chat);
        // 返回结果
        session.getBasicRemote().sendText(chatJson);
        // 判断目标用户是否在线
        if (webSocketMap.containsKey(this.targetClientId)) {
            // 推送消息, 消息转换
            chat.setTargetUser(UserVo.of(userService.getById(currUserId)));
            webSocketMap.get(this.targetClientId).getSession().getBasicRemote().sendText(JSON.toJSONString(chat));
        }
    }

    @OnClose
    public void onClose() {
        webSocketMap.remove(this.currUserId);
        log.info("{} websocket close, the remaining {} connections", this.currUserId, webSocketMap.size());
    }

    @OnError
    public void onError(Throwable t) {
        log.error("the user {} client WebSocket Model Error：{}", this.currUserId, t);
    }

}
