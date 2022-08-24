package com.frame.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.frame.business.entity.Cart;
import com.frame.business.entity.OrderProd;
import com.frame.business.entity.ProdOrderCorr;
import com.frame.business.mapper.OrderProdMapper;
import com.frame.business.service.OrderProdService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.frame.business.service.ProdOrderCorrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单商品表 服务实现类
 * </p>
 *
 * @author 邹坤
 * @since 2022-05-02
 */
@Service
public class OrderProdServiceImpl extends ServiceImpl<OrderProdMapper, OrderProd> implements OrderProdService {

    @Autowired
    private ProdOrderCorrService prodOrderCorrService;

    @Override
    public List<OrderProd> selectByIds(List<Long> orderProdIds) {
        QueryWrapper<OrderProd> orderProdQueryWrapper = new QueryWrapper<>();
        orderProdQueryWrapper.in("id", orderProdIds);
        return baseMapper.selectList(orderProdQueryWrapper);
    }

    @Override
    public List<OrderProd> queryByOrderId(Long id) {
        List<ProdOrderCorr> prodOrderCorrs = prodOrderCorrService.selectByOrderId(id);
        List<Long> orderProdIds = prodOrderCorrs.stream()
                .map(prodOrderCorr -> prodOrderCorr.getOrderProdId())
                .collect(Collectors.toList());
        return baseMapper.selectBatchIds(orderProdIds);
    }

}
