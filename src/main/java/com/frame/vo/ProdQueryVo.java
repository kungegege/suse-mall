package com.frame.vo;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/3/17
 * Time: 23:49
 * Description:
 */
@Data
public class ProdQueryVo {
    private String prodName;
    private String userName;
    private String categoryName;
    private Integer status;

    private String userId;
}
