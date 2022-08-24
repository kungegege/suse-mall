package com.frame.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/5/25
 * Time: 17:33
 * Description:
 */
@Data
public class CommentVo {

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
    private ArrayList<Long> prodIds;

    /**
     * 父评论ID （没有为0）
     */
    private String parentId;


    /**
     * 满意度（有效值：1-5）
     */
    private Integer grade;
}
