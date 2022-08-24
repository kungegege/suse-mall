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
 * 用户配送地址
 * </p>
 *
 * @author 邹坤
 * @since 2022-05-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tz_order_addr")
public class OrderAddr implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String realName;

    /**
     * （1是学校地址、2自定义地址）
     */
    private Integer isTemplate;

    private Integer templateId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 地区
     */
    private String region;

    /**
     * 收货人
     */
    private String receiver;

    /**
     * 地址
     */
    private String addr;

    /**
     * 手机
     */
    private String mobile;

    /**
     * 建立时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


    /**
     * 源地址ID
     */
    private Long userAddrId;
}
