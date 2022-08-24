package com.frame.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.frame.business.entity.Chat;
import com.frame.business.entity.Message;
import com.frame.business.entity.User;
import com.frame.business.mapper.ChatMapper;
import com.frame.business.service.ChatService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.frame.business.service.UserService;
import com.frame.utils.RedisTemplateUtil;
import com.frame.vo.ChatMessage;
import com.frame.vo.ChatMessageVo;
import com.frame.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 邹坤
 * @since 2022-05-09
 */
@Service
public class ChatServiceImpl extends ServiceImpl<ChatMapper, Chat> implements ChatService {

    @Autowired
    private UserService userService;

    @Override
    public List<Chat> queryHis(String currentUserId, String tid) {
        QueryWrapper<Chat> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .and(o -> o.eq("dest", tid).eq("user_id", currentUserId))
                .or(o-> o.eq("user_id", tid).eq("dest", currentUserId))
                .orderBy(true, false, "create_time");

        User targetUser = userService.getById(tid);
        List<Chat> chatList = baseMapper.selectList(queryWrapper);
        for (Chat chat : chatList) {
            chat.setTargetUser(UserVo.of(targetUser));
        }
        return chatList;
    }

    @Override
    public Chat saveChatMessage(ChatMessage chatMessage) {
        Chat chat = Chat.of(chatMessage);
        User targetUser = userService.getById(chatMessage.getDest());
        chat.setTargetUser(UserVo.of(targetUser));
        baseMapper.insert(chat);
        return chat;
    }

    @Override
    public void saveChatMessageBatch(List<ChatMessage> chatMessages) {
        for (ChatMessage chatMessage : chatMessages) {
            this.saveChatMessage(chatMessage);
        }
    }

    @Override
    public List<ChatMessageVo> queryLastMsgList(String currentUserId) {
        ArrayList<ChatMessageVo> res = new ArrayList<>();

        QueryWrapper<Chat> chatQueryWrapper = new QueryWrapper<>();
        chatQueryWrapper.select("user_id")
                .eq("dest", currentUserId)
                .groupBy("user_id");
        List<Chat> chats = baseMapper.selectList(chatQueryWrapper);

        for (Chat chat : chats) {
            ChatMessageVo chatMessageVo = queryLastMsg(chat.getUserId(), currentUserId);
            User targetUser = userService.getById(chat.getUserId());
            chatMessageVo.setTargetUser(UserVo.of(targetUser));
            res.add(chatMessageVo);
        }
        return res;
    }


    @Override
    public ChatMessageVo queryLastMsg(String uid, String tid) {
        ChatMessageVo chatMessageVo = baseMapper.queryLastMsg(uid, tid);

        chatMessageVo.setNotReadCount(getNotReadCount(uid, tid));
        return chatMessageVo;
    }

    @Override
    public void updateChatStatus(String tid, String currentUserId, Integer status) {
        UpdateWrapper<Chat> queryWrapper = new UpdateWrapper<>();
        queryWrapper.eq("user_id", tid)
                .eq("dest", currentUserId)
                .set(true, "is_read", status);
        baseMapper.update(null, queryWrapper);
    }

    /**
     * 查询未读消息条数
     * @param uid
     * @param tid
     * @return
     */
    private Integer getNotReadCount(String uid, String tid) {
        QueryWrapper<Chat> chatQueryWrapper = new QueryWrapper<>();
        chatQueryWrapper.eq("user_id", uid)
                .eq("dest", tid)
                .eq("is_read", 1);
        return baseMapper.selectCount(chatQueryWrapper);
    }

}
