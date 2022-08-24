package com.frame.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/5/3
 * Time: 15:09
 * Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderProdVo {

    /**
     * 必传参数
     */
    private Long prodId;

    /**
     * 购买数量
     */
    private Integer prodNum;
}
