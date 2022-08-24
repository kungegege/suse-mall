package com.frame.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/3/3
 * Time: 15:14
 * Description:
 */
@Data
public class ChatMessage {

    private Long id;

    private String msg;

    private String dest;

    private String userId;

    private String type;

    public ChatMessage(String msg, String dest, Integer isRead, String userId) {
        this.msg = msg;
        this.dest = dest;
        this.isRead = isRead;
        this.userId = userId;
        this.type = "text";
    }

    public ChatMessage(String msg, String dest, String userId) {
        this(msg, dest, 1, userId);
    }

    private LocalDateTime createTime = LocalDateTime.now();

    private Integer isRead;
}
