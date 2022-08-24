package com.frame.business.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 用户与角色对应关系
 * </p>
 *
 * @author 邹坤
 * @since 2022-04-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tz_user_role")
@AllArgsConstructor
@NoArgsConstructor
public class UserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 角色ID
     */
    private Integer roleId;

    // 管理员
    public static final Role MANAGE_ADMIN = new Role(1, "manage", "管理员", null, null,null);

    // 商家
    public static final Role SELLER_USER = new Role(2, "seller", "卖家", null, null, null);

    // 普通用户
    public static final Role NORMAL_USER = new Role(3, "user", "用户", null, null, null);
}
