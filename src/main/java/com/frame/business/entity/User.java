package com.frame.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author 邹坤
 * @since 2022-03-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tz_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
      private String userId;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 用户邮箱
     */
    private String userMail;

    /**
     * 登录密码
     */
    private String loginPassword;

    /**
     * 支付密码
     */
    private String payPassword;

    /**
     * 手机号码
     */
    private String userMobile;

    /**
     * 修改时间
     */
    private LocalDateTime modifyTime;

    /**
     * 注册时间
     */
    private LocalDateTime userRegtime;

    /**
     * 注册IP
     */
    private String userRegip;

    /**
     * 最后登录时间
     */
    private LocalDateTime userLasttime;

    /**
     * 最后登录IP
     */
    private String userLastip;

    /**
     * 备注
     */
    private String userMemo;

    /**
     * M(男) or F(女)
     */
    private String sex;

    /**
     * 例如：2009-11-27
     */
    private String birthDate;

    /**
     * 头像图片路径
     */
    private String pic;

    /**
     * 状态 1 正常 0 无效
     */
    private Integer status;

    /**
     * 用户积分
     */
    private Integer score;


}
