package com.frame.business.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 订单与订单商品的对应关系表
 * </p>
 *
 * @author 邹坤
 * @since 2022-05-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tz_prod_order_corr")
public class ProdOrderCorr implements Serializable {

    private static final long serialVersionUID = 1L;

      private Long id;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单商品ID
     */
    private Long orderProdId;


}
