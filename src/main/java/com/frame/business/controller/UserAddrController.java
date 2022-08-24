package com.frame.business.controller;


import com.frame.annotation.RequiresRoles;
import com.frame.business.base.BaseController;
import com.frame.business.entity.UserAddr;
import com.frame.business.service.UserAddrService;
import com.frame.utils.DataResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户配送地址 前端控制器
 * </p>
 * @author 邹坤
 * @since 2022-04-30
 */
@RestController
@RequestMapping("/userAddr")
public class UserAddrController extends BaseController {

    @Autowired
    private UserAddrService userAddrService;

    /**
     * 添加收获地址
     *
     * @return
     */
    @RequiresRoles("user")
    @PostMapping("add")
    public DataResult addUserAddress(@RequestBody UserAddr userAddr) {

        if (userAddr.getIsDefault() == 1) {
            // 重置其他地址
            userAddrService.resetDefaultAddress();
        }

        userAddr.setCreateTime(LocalDateTime.now());
        userAddr.setUpdateTime(LocalDateTime.now());
        userAddr.setUserId(getCurrentUserId());
        userAddrService.addUserAddress(userAddr);
        return DataResult.success();
    }


    @RequiresRoles("user")
    @GetMapping("query/uid")
    public DataResult queryByUId() {
        String userId = getCurrentUserId();
        List<UserAddr> userAddrs = userAddrService.queryByUId(userId);
         userAddrs = userAddrs.stream().sorted((o1, o2) -> {
            return o1.getIsDefault() - o2.getIsDefault();
        }).collect(Collectors.toList());
        return DataResult.success(userAddrs);
    }


    @RequiresRoles("user")
    @PostMapping("edit")
    public DataResult editUserAddress(@RequestBody UserAddr userAddr) {
        if (userAddr.getIsDefault() == 1) {
            // 重置其他地址
            userAddrService.resetDefaultAddress();
        }
        userAddr.setUpdateTime(LocalDateTime.now());
        userAddrService.updateById(userAddr);
        return DataResult.success();
    }

    @RequiresRoles("user")
    @GetMapping("query/default")
    public DataResult queryDefaultAddr() {
        String userId = getCurrentUserId();
        UserAddr userAddrs = userAddrService.queryDefaultAddr(userId);
        return DataResult.success(userAddrs);
    }
}

