package com.frame.business.service;

import com.frame.business.entity.Cart;
import com.baomidou.mybatisplus.extension.service.IService;
import com.frame.vo.CartChgVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 邹坤
 * @since 2022-04-30
 */
public interface CartService extends IService<Cart> {

    void addProdCart(Long prodId, String currentUserId);

    void changeCartNum(CartChgVo cartChgVo);

    /**
     * 查询购物车
     * @param userId
     * @return
     */
    List<Cart> queryCart(String userId);

    void removeCart(Integer id);

    /**
     * 修改购物车商品状态
     * @param status
     * @param userId
     */
    void resetAllStatus(Integer status, String userId);

    /**
     * 清空购物车
     * @param userId
     */
    void clearCart(String userId);

    /**
     * 清除购物车选中的商品
     * @param currentUserId
     */
    void clearCartProd(String currentUserId, Integer status);

}
