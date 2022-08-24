package com.frame.business.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.frame.business.entity.Carousel;
import com.baomidou.mybatisplus.extension.service.IService;
import com.frame.vo.CarouselQueryVo;

import java.util.ArrayList;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 邹坤
 * @since 2022-04-02
 */
public interface CarouselService extends IService<Carousel> {

    Page<Carousel> queryCarouselByPage(CarouselQueryVo carouselQueryVo, Page<Carousel> objectPage);

    void deleteBatch(ArrayList<Long> ids);
}
