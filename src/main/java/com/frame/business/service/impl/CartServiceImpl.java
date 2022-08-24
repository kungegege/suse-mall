package com.frame.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.frame.business.entity.Cart;
import com.frame.business.entity.Prod;
import com.frame.business.mapper.CartMapper;
import com.frame.business.service.CartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.frame.business.service.ProdService;
import com.frame.vo.CartChgVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 邹坤
 * @since 2022-04-30
 */
@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements CartService {

    @Autowired
    private ProdService prodService;

    @Override
    public void addProdCart(Long prodId, String currentUserId) {
        Cart cart = hasExist(prodId, currentUserId);
        if (cart == null) {
            Prod prod = prodService.getById(prodId);
            cart = Cart.ofProd(prod);
            cart.setUserId(currentUserId);
            baseMapper.insert(cart);
        }else {
            cart.setNumber(cart.getNumber() + 1);
            baseMapper.updateById(cart);
        }
    }

    @Override
    public void changeCartNum(CartChgVo cartChgVo) {
        if (cartChgVo.getNumber() < 1) {
            // 移出
            baseMapper.deleteById(cartChgVo.getId());
        } else {
            Cart cart = new Cart();
            BeanUtils.copyProperties(cartChgVo, cart);
            baseMapper.updateById(cart);
        }
    }

    @Override
    public List<Cart> queryCart(String userId) {
        QueryWrapper<Cart> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);

        List<Cart> carts = baseMapper.selectList(wrapper);
        // 计算总价格
        carts.stream().forEach(cart -> {
            Prod newProd = prodService.getById(cart.getProdId());
            cart.setTotalPrice(newProd.getPrice()
                    .multiply(new BigDecimal(cart.getNumber()))
                    .setScale(2,BigDecimal.ROUND_HALF_UP));
        });

        // 获取商品信息
        carts.forEach(cart -> {
            Prod prod = prodService.getById(cart.getProdId());
            cart.setProd(prod);
        });

        return carts;
    }

    @Override
    public void removeCart(Integer id) {
        baseMapper.deleteById(id);
    }

    @Override
    public void resetAllStatus(Integer status, String userId) {
        UpdateWrapper<Cart> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set(true, "status", status)
                .eq("user_id", userId);
        baseMapper.update(null, updateWrapper);
    }

    @Override
    public void clearCart(String userId) {
        clearCartProd(userId, null);
    }

    @Override
    public void clearCartProd(String currentUserId, Integer status) {
        QueryWrapper<Cart> cartQueryWrapper = new QueryWrapper<>();
        cartQueryWrapper.eq("user_id", currentUserId)
                .eq(status != null, "status", status);
        baseMapper.delete(cartQueryWrapper);
    }

    private Cart hasExist(Long prodId, String currentUserId) {
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("prod_id", prodId).eq("user_id", currentUserId);
        Cart cart = baseMapper.selectOne(queryWrapper);
        return cart;
    }

}
