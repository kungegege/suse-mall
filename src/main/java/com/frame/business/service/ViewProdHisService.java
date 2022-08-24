package com.frame.business.service;

import com.frame.business.entity.Prod;
import com.frame.business.entity.ViewProdHis;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 邹坤
 * @since 2022-05-01
 */
public interface ViewProdHisService extends IService<ViewProdHis> {

    void addViewHis(ViewProdHis viewProdHis);

    List<ViewProdHis> queryViewHis(Integer limit,String uid);
}
