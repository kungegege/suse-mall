package com.frame.business.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.frame.business.entity.Prod;
import com.baomidou.mybatisplus.extension.service.IService;
import com.frame.vo.ProdQueryVo;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 商品 服务类
 * </p>
 *
 * @author 邹坤
 * @since 2022-03-15
 */
public interface ProdService extends IService<Prod> {

    List<Prod> queryProductionsByCid(Integer cid);

    List<Prod> getProductionsByCid(Integer id);

    Page<Prod> queryProductionsByPage(Page<Prod> page, ProdQueryVo prodQueryVo);

    Prod insertProd(Prod prod);

    void delBatch(List<Integer> ids);

    Prod getProdById(Long id);

    List<Prod> selectRecentProd(Integer size);

    List<Prod> selectHotProd(Integer limit);

    List<Prod> search(String keyword);

    Page<Prod> searchByPage(String keyword, Page<Prod> objectPage);

    List<Prod> sortSearchByKey(String key, Integer limit);

    List<Prod> getRecommendProd(Integer limit);

    /**
     * 更新库存
     *
     * @param step
     */
    void updateStock(Integer step, Long prodId);

    /**
     * 更新销量
     * @param num
     * @param prodId
     */
    void updateSoldNum(int num, Long prodId);
}
