package com.frame.business.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.frame.vo.ChatMessage;
import com.frame.vo.UserVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

/**
 * <p>
 * </p>
 * @author 邹坤
 * @since 2022-05-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tz_chat")
public class Chat implements Serializable {

    private static final long serialVersionUID = 1L;

      private Long id;

    /**
     * 消息内容
     */
    private String msg;

    /**
     * 目标用户
     */
    private String dest;

    /**
     * 当前用户ID
     */
    private String userId;

    private LocalDateTime createTime;

    /**
     * 是否已读
     */
    private Integer isRead;

    @TableField(exist = false)
    private UserVo targetUser;

    /**
     * 消息类型
     */
    private String type;

    public static Chat of(ChatMessage chatMessage) {
        Chat chat = new Chat();
        chat.setCreateTime(chatMessage.getCreateTime());
        chat.setIsRead(chatMessage.getIsRead());
        chat.setMsg(chatMessage.getMsg());
        chat.setDest(chatMessage.getDest());
        chat.setUserId(chatMessage.getUserId());
        chat.setType(chatMessage.getType());
        return chat;
    }

    public static class ChatStatus {
        public static final Integer NO_READ = 1;
        public static final Integer HAS_READ = 2;
    }

}
