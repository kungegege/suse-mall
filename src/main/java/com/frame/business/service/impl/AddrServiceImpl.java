package com.frame.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.frame.business.entity.Addr;
import com.frame.business.entity.AddrProd;
import com.frame.business.mapper.AddrMapper;
import com.frame.business.service.AddrProdService;
import com.frame.business.service.AddrService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 邹坤
 * @since 2022-04-26
 */
@Service
public class AddrServiceImpl extends ServiceImpl<AddrMapper, Addr> implements AddrService {

    @Resource
    private AddrProdService addrProdService;

    @Override
    public List<Addr> queryAddressByPid(Long id) {
        List<AddrProd> addrProds = addrProdService.queryByPid(id);
        if (addrProds.isEmpty()) {
            return new ArrayList<>();
        }
        QueryWrapper<Addr> wrapper = new QueryWrapper<>();
        wrapper.in("id", addrProds.stream().map(it -> it.getAddrId()).collect(Collectors.toList()));
        return baseMapper.selectList(wrapper);
    }
}
