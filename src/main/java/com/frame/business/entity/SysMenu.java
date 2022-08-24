package com.frame.business.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 菜单管理
 * </p>
 *
 * @author 邹坤
 * @since 2022-04-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tz_sys_menu")
public class SysMenu implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 父菜单ID，一级菜单为0
     */
    private Integer parentId;

    /**
     * 排序
     */
    private Integer orderNumber;

    /**
     * 菜单URL
     */
    private String href;

    /**
     * 菜单名称
     */
    private String title;

    /**
     * 菜单图标
     */
    private String icon;

    private String target;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String createUserId;

    private String createUserName;

    @TableField(exist = false)
    private List<SysMenu> child;

}
