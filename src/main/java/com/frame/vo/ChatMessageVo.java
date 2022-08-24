package com.frame.vo;

import com.frame.business.entity.Chat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/5/10
 * Time: 10:32
 * Description:
 */
@Data
public class ChatMessageVo {

    private LocalDateTime createTime;

    /**
     * 最近的消息
     */
    private String lastMsg;

    private UserVo targetUser;

    /**
     * 未读消息条数
     */
    private Integer notReadCount;

    public static ChatMessageVo of(Chat chat) {
        ChatMessageVo chatMessageVo = new ChatMessageVo();
        chatMessageVo.setTargetUser(chat.getTargetUser());
        chatMessageVo.setCreateTime(chat.getCreateTime());
        chatMessageVo.setLastMsg(chat.getMsg());
        return chatMessageVo;
    }

}
