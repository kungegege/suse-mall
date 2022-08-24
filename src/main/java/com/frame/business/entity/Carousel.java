package com.frame.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author 邹坤
 * @since 2022-04-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tz_carousel")
public class Carousel implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(type = IdType.AUTO)
      private Integer id;

    /**
     * 轮播图地址
     */
    private String img;

    /**
     * 轮播图序号（越小约优先）
     */
    private Integer seq;

    /**
     * 1开启，0禁用
     */
    private Integer status;

    /**
     * 连接对象
     */
    private String link;

    /**
     * 背景颜色（rgb）
     */
    private String background;

    /**
     * 连接类型（1：http地址，2：商品，3:通告）
     */
    private Integer linkType;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String createUser;

    private String lastUpdateUser;


}
