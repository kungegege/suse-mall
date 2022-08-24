package com.frame.business.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.frame.serializer.BigDecimalFormat;
import com.frame.vo.OrderVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

/**
 * <p>
 *
 * </p>
 *
 * @author 邹坤
 * @since 2022-05-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tz_order")
public class Order implements Serializable, Comparable<Order> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 使用的优惠券
     */
    private String couponId;

    /**
     * 用户名(买家)
     */
    private String userId;

    /**
     * 订单总额（实际付款）
     */
    private BigDecimal totalAmount;

    /**
     * 应付总额
     */
    @JsonSerialize(using = BigDecimalFormat.class)
    private BigDecimal payAmount;

    /**
     * 运费金额
     */
    private BigDecimal freightAmount;

    /**
     * 促销优化金额（促销价、满减、阶梯价）
     */
    private BigDecimal promotionAmount;

    /**
     * 积分抵扣金额
     */
    private BigDecimal integrationAmount;

    /**
     * 优惠券抵扣金额
     */
    private BigDecimal couponAmount;

    /**
     * 后台调整订单使用的折扣金额
     */
    private BigDecimal discountAmount;

    /**
     * 支付方式【1->支付宝；2->微信；3->银联； 4->货到付款；】
     */
    private Integer payType;

    /**
     * 订单来源[0->PC订单；1->app订单]
     */
    private Integer sourceType;

    /**
     * 订单状态【0->待付款；1->待收货；2->待评价 3->已完成】
     */
    private Integer status;

    /**
     * 物流公司(配送方式)
     */
    private String deliveryCompany;

    /**
     * 物流单号
     */
    private String deliverySn;

    /**
     * 自动确认时间（天）
     */
    private Integer autoConfirmDay;

    /**
     * 可以获得的积分
     */
    private Integer integration;

    /**
     * 可以获得的成长值
     */
    private Integer growth;

    /**
     * 发票类型[0->不开发票；1->电子发票；2->纸质发票]
     */
    private Integer billType;

    /**
     * 发票抬头
     */
    private String billHeader;

    /**
     * 发票内容
     */
    private String billContent;

    /**
     * 收票人电话
     */
    private String billReceiverPhone;

    /**
     * 收票人邮箱
     */
    private String billReceiverEmail;

    /**
     * 收货人姓名
     */
    private String receiverName;

    /**
     * 收货人电话
     */
    private String receiverPhone;

    /**
     * 收货人邮编
     */
    private String receiverPostCode;

    /**
     * 省份/直辖市
     */
    private String receiverProvince;

    /**
     * 城市
     */
    private String receiverCity;

    /**
     * create_time
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createTime;

    /**
     * 备注
     */
    private String remake;

    @TableField(exist = false)
    private User createUser;

    @TableField(exist = false)
    private List<OrderProd> orderProds;

    @Override
    public int compareTo(Order o) {
        if (this.createTime.isBefore(o.getCreateTime())) {
            return 1;
        }
        return -1;
    }

    /**
     * 订单状态【0->待付款；1->待收货；2->待评价 3->已完成】
     */
    public static class OrderStatus {
        public static final Integer NO_PAY = 0;
        public static final Integer NO_RECEIVE = 1;
        public static final Integer NO_COMMENT = 2;
        public static final Integer HAS_FINISHED = 3;
    }

    /**
     * 收货地址ID
     */
    private Long orderAddrId;

    @TableField(exist = false)
    private OrderAddr orderAddr;


}
