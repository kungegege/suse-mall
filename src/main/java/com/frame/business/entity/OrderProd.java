package com.frame.business.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 订单商品表
 * </p>
 *
 * @author 邹坤
 * @since 2022-05-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tz_order_prod")
public class OrderProd implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 产品ID
     */
      @TableId(value = "id", type = IdType.AUTO)
      private Long id;

    private Long prodId;

    /**
     * 商品名称
     */
    private String prodName;

    /**
     * 用户（买家）id
     */
    private String userId;

    /**
     * 原价
     */
    private BigDecimal oriPrice;

    /**
     * 现价
     */
    private BigDecimal price;

    /**
     * 简要描述,卖点等
     */
    private String brief;


    /**
     * 商品主图 （预览图）
     */
    private String pic;

    /**
     * 商品图片，以,分割
     */
    private String imgs;

    /**
     * 商品分类
     */
    private Long categoryId;

    /**
     *  购买数量
     */
    private Integer number;

    /**
     * 录入时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 上架时间
     */
    private LocalDateTime putawayTime;

    /**
     * 邮费
     */
    private BigDecimal deliveryPrice;


}
