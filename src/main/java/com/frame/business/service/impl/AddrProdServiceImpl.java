package com.frame.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.frame.business.entity.AddrProd;
import com.frame.business.mapper.AddrProdMapper;
import com.frame.business.service.AddrProdService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 邹坤
 * @since 2022-04-26
 */
@Service
public class AddrProdServiceImpl extends ServiceImpl<AddrProdMapper, AddrProd> implements AddrProdService {

    @Override
    public List<AddrProd> queryByPid(Long id) {
        QueryWrapper<AddrProd> wrapper = new QueryWrapper<>();
        wrapper.eq("prod_id", id);
        return baseMapper.selectList(wrapper);
    }
}
