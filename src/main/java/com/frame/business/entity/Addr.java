package com.frame.business.entity;

import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("tz_addr")
public class Addr implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 地址
     */
    private String address;

    /**
     * 备注
     */
    private String  remark;
}
