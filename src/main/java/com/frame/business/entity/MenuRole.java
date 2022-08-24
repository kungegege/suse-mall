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
 * @since 2022-04-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tz_menu_role")
public class MenuRole implements Serializable {

    private static final long serialVersionUID = 1L;

      private String id;

    private Integer menuId;

    private Integer roleId;


}
