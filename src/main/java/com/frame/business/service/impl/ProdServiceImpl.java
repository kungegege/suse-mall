package com.frame.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.frame.business.entity.Prod;
import com.frame.business.entity.User;
import com.frame.business.mapper.ProdMapper;
import com.frame.business.service.CategoryService;
import com.frame.business.service.ProdService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.frame.business.service.UserService;
import com.frame.vo.ProdQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 商品 服务实现类
 * </p>
 *
 * @author 邹坤
 * @since 2022-03-15
 */
@Service
public class ProdServiceImpl extends ServiceImpl<ProdMapper, Prod> implements ProdService {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;


    @Override
    public List<Prod> queryProductionsByCid(Integer cid) {
        QueryWrapper<Prod> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1)
                .eq("category_id", cid);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public List<Prod> getProductionsByCid(Integer id) {
        QueryWrapper<Prod> wrapper = new QueryWrapper<>();

        List<Integer> ids = categoryService.selectIdsAll(id);

        if (ids.isEmpty()) {
            return new ArrayList<>();
        }

        wrapper.eq("status", 1).in("category_id", ids);

        return baseMapper.selectList(wrapper);
    }

    @Override
    public Page<Prod> queryProductionsByPage(Page<Prod> page, ProdQueryVo prodQueryVo) {
        Integer status = prodQueryVo.getStatus();
        String name = prodQueryVo.getProdName();
        String userName = prodQueryVo.getUserName();
        String categoryName = prodQueryVo.getCategoryName();

        QueryWrapper<Prod> wrapper = new QueryWrapper<>();

        wrapper.eq(status != null, "status", status)
                .like(!StringUtils.isEmpty(name), "prod_name", name)
                .eq(prodQueryVo.getUserId() != null, "user_id", prodQueryVo.getUserId());

        if (!StringUtils.isEmpty(categoryName)) {
            List<Long> categoriesId = categoryService.queryByName(categoryName)
                    .stream().map(c -> c.getCategoryId())
                    .collect(Collectors.toList());
            wrapper.in(!categoriesId.isEmpty(), "category_id", categoriesId);
        }

        if (!StringUtils.isEmpty(userName)) {
            List<String> uIds = userService.queryByName(userName).stream().map(User::getUserId).collect(Collectors.toList());
            wrapper.in(!uIds.isEmpty(), "user_id", uIds);
        }

        return baseMapper.selectPage(page, wrapper);
    }

    @Override
    public Prod insertProd(Prod prod) {
        prod.setCreateTime(LocalDateTime.now());
        prod.setUpdateTime(LocalDateTime.now());
        if (prod.getStatus() != null && prod.getStatus() == 1) {
            prod.setPutawayTime(LocalDateTime.now());
        }
        baseMapper.insert(prod);
        return prod;
    }

    /**
     * 批量删除
     *
     * @param ids
     */
    @Override
    public void delBatch(List<Integer> ids) {
        QueryWrapper<Prod> prodQueryWrapper = new QueryWrapper<>();
        if (!ids.isEmpty()) {
            prodQueryWrapper.in("prod_id", ids);
            baseMapper.delete(prodQueryWrapper);
        }
    }

    @Override
    public Prod getProdById(Long id) {
        return baseMapper.selectById(id);
    }

    @Override
    public List<Prod> selectRecentProd(Integer size) {
        if (size==null || size<=0){
            size = 10;
        }
        QueryWrapper<Prod> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        queryWrapper.eq("status", 1);
        queryWrapper.last("limit " + size);
        List<Prod> prods = baseMapper.selectList(queryWrapper);
        return prods;
    }

    @Override
    public List<Prod> selectHotProd(Integer size) {
        if (size==null || size<=0){
            size = 10;
        }
        QueryWrapper<Prod> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("sold_num");
        queryWrapper.eq("status", 1);
        queryWrapper.last("limit " + size);
        List<Prod> prods = baseMapper.selectList(queryWrapper);
        return prods;
    }

    @Override
    public List<Prod> search(String keyword) {
        QueryWrapper<Prod> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1)
                .like("prod_name", keyword)
                .or().like("prod_id", keyword)
                .or().like("brief", keyword)
                .or().like("content", keyword);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public Page<Prod> searchByPage(String keyword, Page<Prod> objectPage) {
        QueryWrapper<Prod> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1)
                .like("prod_name", keyword)
                .or().like("brief", keyword)
                .or().like("content", keyword);
        return baseMapper.selectPage(objectPage, queryWrapper);
    }

    @Override
    public List<Prod> sortSearchByKey(String key, Integer limit) {
        QueryWrapper<Prod> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        queryWrapper.orderByDesc(key);
        queryWrapper.last("limit " + limit);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<Prod> getRecommendProd(Integer limit) {
        QueryWrapper<Prod> qw = new QueryWrapper<>();
        qw.eq("status", 1);

        return  baseMapper.selectList(qw);
    }

    @Override
    public void updateStock(Integer step, Long prodId) {
        // 修改库存 TODO 并发问题-原子性
        Prod prod = baseMapper.selectById(prodId);
        if (prod.getTotalStocks() + step < 0) {
            throw new InvalidParameterException("库存不能为负数");
        }
        UpdateWrapper<Prod> prodUpdateWrapper = new UpdateWrapper<>();
        prodUpdateWrapper.set(true, "total_stocks", prod.getTotalStocks() + step)
                .eq("prod_id", prodId);
        baseMapper.update(null, prodUpdateWrapper);
    }

    @Override
    public void updateSoldNum(int num, Long prodId) {
        Prod prod = baseMapper.selectById(prodId);
        UpdateWrapper<Prod> prodUpdateWrapper = new UpdateWrapper<>();
        prodUpdateWrapper.set(true, "sold_num", prod.getSoldNum() + num)
                .eq("prod_id", prodId);
        baseMapper.update(null, prodUpdateWrapper);
    }

}
