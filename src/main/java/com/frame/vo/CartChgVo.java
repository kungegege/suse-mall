package com.frame.vo;

import com.frame.business.entity.Cart;
import lombok.Data;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/4/30
 * Time: 11:20
 * Description:
 */
@Data
public class CartChgVo {
    private Integer id;
    private Integer number;
    private String userId;
    private Long prodId;
}
