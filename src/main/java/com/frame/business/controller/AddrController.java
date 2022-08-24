package com.frame.business.controller;


import com.frame.business.entity.Addr;
import com.frame.business.service.AddrService;
import com.frame.utils.DataResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 邹坤
 * @since 2022-04-26
 */
@RestController
@RequestMapping("/addr")
public class AddrController {

    @Autowired
    private AddrService addrService;

    /**
     * 商品ID
     * @param id
     * @param response
     * @return
     */
    @GetMapping("/prod/{id}")
    public DataResult queryAddressByPid(@PathVariable Long id, HttpServletResponse response) {
        List<Addr> res = addrService.queryAddressByPid(id);
        return DataResult.success(res);
    }

}

