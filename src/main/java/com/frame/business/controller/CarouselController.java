package com.frame.business.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.frame.business.base.BaseController;
import com.frame.business.entity.Carousel;
import com.frame.business.entity.Category;
import com.frame.business.service.CarouselService;
import com.frame.utils.DataResult;
import com.frame.vo.CarouselQueryVo;
import com.frame.vo.CategoryQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * <p>
 *  前端控制器   TODO 权限校验
 * </p>
 * @author 邹坤
 * @since 2022-04-02
 */
@RestController
@RequestMapping("/carousel")
public class CarouselController extends BaseController {

    @Autowired
    private CarouselService carouselService;

    @GetMapping("/page")
    public DataResult queryCategoryByPage(Integer page, Integer limit, CarouselQueryVo carouselQueryVo) {
        Page<Carousel> prods = carouselService.queryCarouselByPage(carouselQueryVo, new Page<>(page, limit));
        return DataResult.success(prods);
    }

    @GetMapping("query/{id}")
    public DataResult queryById(@PathVariable Long id) {
        Carousel carousel = carouselService.getById(id);
        return DataResult.success(carousel);
    }

    @PostMapping("update/id")
    public DataResult updateById(@RequestBody CarouselQueryVo carouselQueryVo) {
        Carousel carousel = new Carousel();
        carousel.setUpdateTime(LocalDateTime.now());
        carousel.setLastUpdateUser(getCurrentUserId());
        BeanUtils.copyProperties(carouselQueryVo, carousel);
        carouselService.updateById(carousel);
        return DataResult.success();
    }


    @RequestMapping("delete/batch")
    public DataResult deleteBatch(@RequestParam("ids") ArrayList<Long> ids) {
        carouselService.deleteBatch(ids);
        return DataResult.success();
    }


    @PostMapping("save")
    public DataResult save(@RequestBody Carousel carousel) {

        carousel.setUpdateTime(LocalDateTime.now());
        carousel.setCreateUser(getCurrentUserId());
        carousel.setCreateTime(LocalDateTime.now());

        carouselService.save(carousel);
        return DataResult.success();
    }


}

