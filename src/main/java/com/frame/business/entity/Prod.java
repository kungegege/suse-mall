package com.frame.business.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.frame.serializer.BigDecimalFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 商品
 * </p>
 *
 * @author 邹坤
 * @since 2022-03-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tz_prod")
public class Prod implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 产品ID
     */
      @TableId(value = "prod_id", type = IdType.AUTO)
    private Long prodId;

    /**
     * 商品名称
     */
    private String prodName;

    /**
     * 用户（卖家）id
     */
    private String  userId;

    /**
     * 原价
     */
    private BigDecimal oriPrice;

    /**
     * 现价
     */
    @JsonSerialize(using = BigDecimalFormat.class)
    private BigDecimal price;

    /**
     * 简要描述,卖点等
     */
    private String brief;

    /**
     * 详细描述
     */
    private String content;

    /**
     * 商品主图 （预览图）
     */
    private String pic;

    /**
     * 商品图片，以,分割
     */
    private String imgs;

    /**
     * 默认是1，表示正常状态, -1表示删除, 0下架
     */
    private Integer status;

    /**
     * 商品分类
     */
    private Long categoryId;

    /**
     * 销量
     */
    private Integer soldNum;

    /**
     * 总库存
     */
    private Integer totalStocks;

    /**
     * 配送方式json见TransportModeVO
     */
    private String deliveryMode;

    /**
     * 属性
     */
    private String attr;

    /**
     * 运费模板id
     */
    private Long deliveryTemplateId;

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
     * 版本 乐观锁
     */
    private Integer version;


    /**
     * 商家备注
     */
    private String remake;
}
