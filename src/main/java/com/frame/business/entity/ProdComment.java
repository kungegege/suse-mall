package com.frame.business.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.frame.vo.CommentVo;
import com.frame.vo.UserVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 商品评论表
 * </p>
 * @author 邹坤
 * @since 2022-05-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tz_prod_comment")
public class ProdComment implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private String id;

    /**
     * 内容
     */
    private String content;

    /**
     * 图片多张图片,分割
     */
    private String imgs;

    /**
     * 商品ID
     */
    private Long prodId;

    /**
     * 父评论ID （没有为0）
     */
    private String parentId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createTime;

    /**
     * 满意度（有效值：1-5）
     */
    private Integer grade;

    /**
     * 浏览次数
     */
    private Integer browseTimes;

    /**
     * 1-正常，2-待审核，3-删除
     */
    private Integer status;


    @TableField(exist = false)
    private UserVo userVo;

    public static ProdComment of(CommentVo commentVo) {
        ProdComment prodComment = new ProdComment();
        prodComment.setCreateTime(LocalDateTime.now());
        prodComment.setBrowseTimes(0);
        prodComment.setGrade(commentVo.getGrade());
        prodComment.setContent(commentVo.getContent());
        prodComment.setImgs(commentVo.getImgs());
        return prodComment;
    }
}
