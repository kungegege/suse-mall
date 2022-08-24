package com.frame.vo;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/5/4
 * Time: 10:25
 * Description:
 */
@Data
public class OrderQueryVo {
    private String username;
    private Integer orderStatus;

    private String userId;
}
