package com.frame.business.controller;


import com.frame.annotation.RequiresRoles;
import com.frame.business.base.BaseController;
import com.frame.business.entity.Cart;
import com.frame.business.service.CartService;
import com.frame.utils.DataResult;
import com.frame.vo.CartChgVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 邹坤
 * @since 2022-04-30
 */
@RestController
@RequestMapping("/cart")
public class CartController extends BaseController {

    @Autowired
    private CartService cartService;



    @GetMapping("/chg")
    @RequiresRoles("user")
    public DataResult changeCartNum(CartChgVo cartChgVo) {
        cartService.changeCartNum(cartChgVo);
        return DataResult.success();
    }

    /**
     * 添加商品到购物车
     * @param prodId
     * @return
     */
    @GetMapping("/add/{prodId}")
    @RequiresRoles("user")
    public DataResult addProd(@PathVariable Long prodId) {
        cartService.addProdCart(prodId, getCurrentUserId());
        return DataResult.success();
    }
    /**
     * 查询购物车商品数据
     * @return
     */
    @GetMapping("/query")
    @RequiresRoles("user")
    public DataResult queryCart(){
        String userId = getCurrentUserId();
        List<Cart> carts = cartService.queryCart(userId);
        return DataResult.success(carts);
    }
    /**
     * 修改购物车商品状态
     * @param status
     * @param id
     * @return
     */
    @GetMapping("/set/status")
    @RequiresRoles
    public DataResult updateStatus(@RequestParam("status") Integer status, Integer id) {
        Cart cart = new Cart();
        cart.setStatus(status);
        cart.setId(id);
        cartService.updateById(cart);
        return DataResult.success();
    }

    /**
     * 移出商品
     * @param id
     * @return
     */
    @GetMapping("/del/{id}")
    @RequiresRoles("user")
    public DataResult removeCart(@PathVariable Integer id){
        cartService.removeCart(id);
        return DataResult.success();
    }

    @GetMapping("/setNum")
    @RequiresRoles
    public DataResult updateNum(@RequestParam("number") Integer num, Integer id) {
        if (num<1){
            this.removeCart(id);
        }else {
            Cart cart = new Cart();
            cart.setNumber(num);
            cart.setId(id);
            cartService.updateById(cart);
        }
        return DataResult.success();
    }




    @GetMapping("/set/all/status")
    @RequiresRoles
    public DataResult updateStatus(@RequestParam("status") Integer status) {
        String userId = getCurrentUserId();
        cartService.resetAllStatus(status, userId);
        return DataResult.success();
    }
    @GetMapping("/clear")
    @RequiresRoles
    public DataResult clearCart() {
        String userId = getCurrentUserId();
        cartService.clearCart(userId);
        return DataResult.success();
    }

}

