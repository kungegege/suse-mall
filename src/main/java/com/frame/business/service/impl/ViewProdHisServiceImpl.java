package com.frame.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.frame.business.entity.Prod;
import com.frame.business.entity.ViewProdHis;
import com.frame.business.mapper.ViewProdHisMapper;
import com.frame.business.service.ProdService;
import com.frame.business.service.ViewProdHisService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 邹坤
 * @since 2022-05-01
 */
@Service
public class ViewProdHisServiceImpl extends ServiceImpl<ViewProdHisMapper, ViewProdHis> implements ViewProdHisService {

    @Autowired
    private ProdService prodService;

    @Override
    public void addViewHis(ViewProdHis viewProdHis) {

        // 查看是否存在该商品的访问记录，存在这跟新访问时间
        QueryWrapper<ViewProdHis> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", viewProdHis.getUserId())
                .eq("prod_id", viewProdHis.getProdId());

        ViewProdHis his = baseMapper.selectOne(wrapper);
        if (his != null) {
            his.setCreateTime(LocalDateTime.now());
            this.updateById(his);
            return;
        }
        viewProdHis.setCreateTime(LocalDateTime.now());
        baseMapper.insert(viewProdHis);
    }

    @Override
    public List<ViewProdHis> queryViewHis(Integer limit, String uid) {
        QueryWrapper<ViewProdHis> hisQueryWrapper = new QueryWrapper<>();
        hisQueryWrapper.eq("user_id", uid).orderBy(true
                ,false,"create_time")
                .last("limit " + limit);

        List<ViewProdHis> viewProdHis = baseMapper.selectList(hisQueryWrapper);

        viewProdHis.forEach(it->{
            Prod prod = prodService.getById(it.getProdId());
            it.setProd(prod);
        });

        return viewProdHis;
    }
}
