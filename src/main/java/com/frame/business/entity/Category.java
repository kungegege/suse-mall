package com.frame.business.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 产品类目
 * </p>
 *
 * @author 邹坤
 * @since 2022-03-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tz_category")
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 类目ID
     */
      @TableId(value = "category_id", type = IdType.AUTO)
    private Long categoryId;

    /**
     * 店铺ID
     */
    private Long shopId;

    /**
     * 父节点
     */
    private Long parentId;

    /**
     * 产品类目名称
     */
    private String categoryName;

    /**
     * 类目图标
     */
    private String icon;

    /**
     * 类目的显示图片
     */
    private String pic;

    /**
     * 排序
     */
    private Integer seq;

    /**
     * 默认是1，表示正常状态,0为下线状态
     */
    private Integer status;

    /**
     * 记录时间
     */
    private LocalDateTime recTime;

    /**
     * 分类层级
     */
    private Integer grade;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
