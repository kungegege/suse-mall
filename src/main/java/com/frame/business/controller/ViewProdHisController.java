package com.frame.business.controller;


import com.frame.annotation.RequiresRoles;
import com.frame.business.base.BaseController;
import com.frame.business.entity.Prod;
import com.frame.business.entity.ViewProdHis;
import com.frame.business.service.ViewProdHisService;
import com.frame.utils.DataResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 *  前端控制器 商品浏览记录
 * </p>
 *
 * @author 邹坤
 * @since 2022-05-01
 */
@RestController
@RequestMapping("/viewProdHis")
public class ViewProdHisController  extends BaseController {

    @Autowired
    private ViewProdHisService viewProdHisService;


    /**
     * 添加浏览记录
     * @param pid
     * @return
     */
    @GetMapping("/add/{pid}")
    public DataResult addViewHis(@PathVariable Long pid) {
        String userId;
        try {
            userId = getCurrentUserId();
        } catch (Exception e) {
             return DataResult.success();
        }
        ViewProdHis viewProdHis = new ViewProdHis();
        viewProdHis.setProdId(pid);
        viewProdHis.setUserId(userId);
        viewProdHisService.addViewHis(viewProdHis);
        return DataResult.success();
    }

    /**
     * 查看浏览记录
     * @param limit
     * @return
     */
    @GetMapping("/query/{limit}")
    public DataResult queryViewHis(@PathVariable Integer limit) {
        String uid = null;
        try {
            uid = getCurrentUserId();
        } catch (Exception e) {
            return DataResult.success(Collections.emptyList());
        }
        List<ViewProdHis> prodList = viewProdHisService.queryViewHis(limit,uid);
        return DataResult.success(prodList);
    }

}

