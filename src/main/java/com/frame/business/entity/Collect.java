package com.frame.business.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *     收藏夹
 * </p>
 * @author 邹坤
 * @since 2022-05-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tz_collect")
public class Collect implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long prodId;

    private String userId;

    public Collect() {}

    public Collect(Long prodId, String userId) {
        this.prodId = prodId;
        this.userId = userId;
        this.createTime = LocalDateTime.now();
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createTime;
}
