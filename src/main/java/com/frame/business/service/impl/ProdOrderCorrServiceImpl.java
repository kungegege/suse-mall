package com.frame.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.frame.business.entity.ProdOrderCorr;
import com.frame.business.mapper.ProdOrderCorrMapper;
import com.frame.business.service.ProdOrderCorrService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 订单与订单商品的对应关系表 服务实现类
 * </p>
 *
 * @author 邹坤
 * @since 2022-05-02
 */
@Service
public class ProdOrderCorrServiceImpl extends ServiceImpl<ProdOrderCorrMapper, ProdOrderCorr> implements ProdOrderCorrService {

    @Override
    public List<ProdOrderCorr> selectByOrderId(Long id) {
        QueryWrapper<ProdOrderCorr> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", id);
        return baseMapper.selectList(queryWrapper);
    }
}
