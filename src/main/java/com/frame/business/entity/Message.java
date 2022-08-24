package com.frame.business.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 评论信息
 * </p>
 * @author 邹坤
 * @since 2022-03-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tz_message")
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 留言创建时间
     */
    private LocalDateTime createTime;

    /**
     *  用户名
     */
    private String userName;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 留言内容
     */
    private String content;

    /**
     * 状态：0:未审核 1审核通过
     */
    private Integer status;

    /**
     * 点赞数
     */
    private Integer approve;

    /**
     * 图片地址,分割
     */
    private String imgs;

    /**
     * 评论商品Id
     */
    private Long targetId;


}
