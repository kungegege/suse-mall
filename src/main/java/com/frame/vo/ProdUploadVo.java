package com.frame.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/3/20
 * Time: 14:43
 * Description:
 */
@Data
public class ProdUploadVo {
    /**
     * 商品名称
     */
    private String prodName;

    private Long prodId;

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
     * 总库存
     */
    private Integer totalStocks;


    /**
     * 属性
     */
    private String attr;

    /**
     * 店铺地址
     */
    private Integer address;


    /**
     * 商家备注
     */
    private String remake;

}
