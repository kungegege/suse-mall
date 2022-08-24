package com.frame.business.controller;


import com.frame.annotation.RequiresRoles;
import com.frame.business.base.BaseController;
import com.frame.business.entity.Collect;
import com.frame.business.service.CollectService;
import com.frame.utils.DataResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 前端控制器 (收藏夹)
 * </p>
 * @author 邹坤
 * @since 2022-05-07
 */
@RestController
@RequestMapping("/collect")
public class CollectController extends BaseController {

    @Autowired
    private CollectService collectService;

    @RequiresRoles
    @GetMapping("change/{pid}")
    public DataResult addCollection(@PathVariable Long pid) {
        Collect collect = collectService.saveAndUpdate(pid, getCurrentUserId());
        return DataResult.success(collect);
    }

    /**
     * 获取收藏商品列表
     * @return
     */
    @RequiresRoles
    @GetMapping("query")
    public DataResult query() {
        String userId = getCurrentUserId();
        List<Collect> res = collectService.queryByUid(userId);
        return DataResult.success(res);
    }

    @RequiresRoles
    @GetMapping("query/{pid}")
    public DataResult query(@PathVariable Long pid ) {
        Collect collect = collectService.query(pid, getCurrentUserId());
        return DataResult.success(collect);
    }

    @RequiresRoles
    @GetMapping("delete/{pid}")
    public DataResult delete(@PathVariable Long pid) {
        collectService.deleteByPid(getCurrentUserId(), pid);
        return DataResult.success();
    }

}

