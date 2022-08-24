package com.frame.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/5/2
 * Time: 15:43
 * Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderVo {

    private List<OrderProdVo> prodVos;

    /**
     * 支付方式
     */
    private Integer payType;

    /**
     * 备注
     */
    private String remake;

    /**
     * 订单状态
     */
    private Integer status;

    /**
     * 付款金额
     */
    private BigDecimal payAmount;
}
