package com.frame.business.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * </p>
 * @author 邹坤
 * @since 2022-04-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tz_cart")
public class Cart implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String userId;

    private Long prodId;

    /**
     * 商品数量
     */
    private Integer number;

    /**
     * 收录时的商品价格
     */
    private BigDecimal oldPrice;

    /**
     * 收录时的商品销售量
     */
    private Integer oldSoldNum;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    /**
     * 状态, 是否被选中（1-选中）
     */
    private Integer status;

    /**
     * 静态构造
     */
    public static Cart ofProd(Prod prod) {
        Cart cart = new Cart();
        cart.setCreateTime(LocalDateTime.now());
        cart.setUpdateTime(LocalDateTime.now());
        cart.setProdId(prod.getProdId());
        cart.setNumber(1);
        cart.setOldSoldNum(prod.getSoldNum());
        cart.setOldPrice(prod.getPrice());
        return cart;
    }

    @TableField(exist = false)
    private BigDecimal totalPrice;

    @TableField(exist = false)
    public Prod prod;

    /**
     *  状态, 是否被选中（1-选中）
     */
    public static final Integer ACTIVE_STATUS = 1;
}
