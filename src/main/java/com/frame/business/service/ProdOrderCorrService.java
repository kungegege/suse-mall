package com.frame.business.service;

import com.frame.business.entity.ProdOrderCorr;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 订单与订单商品的对应关系表 服务类
 * </p>
 *
 * @author 邹坤
 * @since 2022-05-02
 */
public interface ProdOrderCorrService extends IService<ProdOrderCorr> {

    List<ProdOrderCorr> selectByOrderId(Long id);
}
