package com.frame.business.controller;


import com.frame.annotation.RequiresRoles;
import com.frame.business.base.BaseController;
import com.frame.business.entity.Chat;
import com.frame.business.service.ChatService;
import com.frame.utils.DataResult;
import com.frame.vo.ChatMessageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 邹坤
 * @since 2022-05-09
 */
@RestController
@RequestMapping("/chat")
public class ChatController extends BaseController {

    @Autowired
    private ChatService chatService;

    /**
     * 查询历史记录
     * @return
     */
    @RequiresRoles
    @GetMapping("query/his/{tid}")
    public DataResult queryHis(@PathVariable String tid) {
        String currentUserId = getCurrentUserId();
        List<Chat> chats = chatService.queryHis(currentUserId, tid);
        Collections.sort(chats,(o1, o2) -> o1.getCreateTime().isBefore(o2.getCreateTime()) ? -1 : 1);
        return DataResult.success(chats);
    }

    /**
     * 查询消息列表
     * @return
     */
    @GetMapping("msgs")
    @RequiresRoles
    public DataResult queryMsgList() {
        List<ChatMessageVo> chatMessageVos = chatService.queryLastMsgList(getCurrentUserId());
        Collections.sort(chatMessageVos, (o1, o2) -> o1.getCreateTime().isBefore(o2.getCreateTime()) ? 1 : -1);
        return DataResult.success(chatMessageVos);
    }

    /**
     * 批量修改用户状态
     * @param tid 目标用户ID
     * @return
     */
    @GetMapping("/update/batch/status/{tid}")
    @RequiresRoles
    public DataResult updateMsgStatus(@PathVariable String tid) {
        String currentUserId = getCurrentUserId();
        chatService.updateChatStatus(tid, currentUserId, Chat.ChatStatus.HAS_READ);
        return DataResult.success();
    }

    @GetMapping("/update/status/{mid}")
    @RequiresRoles
    public DataResult updateMsgStatus(@PathVariable Long mid) {
        Chat chat = new Chat();
        chat.setId(mid);
        chat.setIsRead(Chat.ChatStatus.HAS_READ);
        chatService.updateById(chat);
        return DataResult.success();
    }



}

