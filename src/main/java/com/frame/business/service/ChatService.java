package com.frame.business.service;

import com.frame.business.entity.Chat;
import com.baomidou.mybatisplus.extension.service.IService;
import com.frame.vo.ChatMessage;
import com.frame.vo.ChatMessageVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 邹坤
 * @since 2022-05-09
 */
public interface ChatService extends IService<Chat> {

    /**
     * 查询历史聊天记录
     */
    List<Chat> queryHis(String currentUserId, String tid);

    /**
     * 持久化聊天内容
     * @param chatMessage
     * @return
     */
    Chat saveChatMessage(ChatMessage chatMessage);

    /**
     * 持久化聊天内容(批量)
     * @param chatMessages
     */
    void saveChatMessageBatch(List<ChatMessage> chatMessages);

    List<ChatMessageVo> queryLastMsgList(String s);

    ChatMessageVo queryLastMsg(String uid, String tid);

    /**
     * 更新消息状态
     * @param tid
     * @param currentUserId
     * @param hasRead
     */
    void updateChatStatus(String tid, String currentUserId, Integer hasRead);

}
