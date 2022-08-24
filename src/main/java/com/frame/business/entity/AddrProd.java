package com.frame.business.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author 邹坤
 * @since 2022-04-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tz_addr_prod")
public class AddrProd implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 商品ID
     */
    private Long prodId;

    /**
     * 地址ID
     */
    private Integer addrId;

    public AddrProd() {
    }

    public AddrProd(Long prodId, Integer addrId) {
        this.prodId = prodId;
        this.addrId = addrId;
    }
}
