package com.frame.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.frame.business.entity.Carousel;
import com.frame.business.mapper.CarouselMapper;
import com.frame.business.service.CarouselService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.frame.vo.CarouselQueryVo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 邹坤
 * @since 2022-04-02
 */
@Service
public class CarouselServiceImpl extends ServiceImpl<CarouselMapper, Carousel> implements CarouselService {

    @Override
    public Page<Carousel> queryCarouselByPage(CarouselQueryVo carouselQueryVo, Page<Carousel> carouselPage) {
        Integer id = carouselQueryVo.getId();
        Integer seq = carouselQueryVo.getSeq();
        Long createUser = carouselQueryVo.getCreateUser();
        Integer status = carouselQueryVo.getStatus();
        Integer linkType = carouselQueryVo.getLinkType();
        QueryWrapper<Carousel> wrapper = new QueryWrapper<>();

        wrapper.eq(id != null, "id", id)
                .eq(createUser != null, "create_user", createUser)
                .eq(seq != null, "seq", seq)
                .eq(status != null, "status", status)
                .eq(linkType != null, "link_type", linkType);
        return baseMapper.selectPage(carouselPage, wrapper);
    }

    @Override
    public void deleteBatch(ArrayList<Long> ids) {
        baseMapper.deleteBatchIds(ids);
    }



}
