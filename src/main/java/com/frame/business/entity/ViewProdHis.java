package com.frame.business.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author 邹坤
 * @since 2022-05-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tz_view_prod_his")
public class ViewProdHis implements Serializable {

    private static final long serialVersionUID = 1L;

      private Long id;

    private String userId;

    private Long prodId;

    private LocalDateTime createTime;

    @TableField(exist = false)
    private Prod prod;


}
