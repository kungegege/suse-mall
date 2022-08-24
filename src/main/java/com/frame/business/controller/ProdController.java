package com.frame.business.controller;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.frame.business.base.BaseController;
import com.frame.business.entity.AddrProd;
import com.frame.business.entity.Prod;
import com.frame.business.entity.User;
import com.frame.business.entity.UserRole;
import com.frame.business.service.AddrProdService;
import com.frame.business.service.ProdService;
import com.frame.utils.DataResult;
import com.frame.vo.ProdQueryVo;
import com.frame.vo.ProdUploadVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;



/**
 * Created with IntelliJ IDEA.
 * @author: HP-zouKun
 * Date: 2022/5/3
 * Time: 22:53
 * Description: 商品模块
 */
@RestController
@RequestMapping("/prod")
public class ProdController extends BaseController {

    @Autowired
    private ProdService prodService;

    @Autowired
    private AddrProdService addrProdService;

    /**
     * 商品分类查询  （查询指定类型的商品）
     * @param id
     * @return
     */
    @GetMapping("/category/{id}")
    public DataResult getProductions(@PathVariable Integer id) {
        List<Prod> prods = prodService.getProductionsByCid(id);
        return DataResult.success(prods);
    }

    @GetMapping("/cateId/{id}")
    public DataResult getProductionsByCid(@PathVariable Integer id) {
        List<Prod> prods = prodService.queryProductionsByCid(id);
        return DataResult.success(prods);
    }


    /**
     * 分页查询
     * @param page
     * @param limit
     * @param prodQueryVo
     * @return
     */
    @GetMapping("/page")
    public DataResult getProductions(Integer page, Integer limit, ProdQueryVo prodQueryVo) {
        if (getCurrentRoles().contains(UserRole.MANAGE_ADMIN.getRoleName())) {
            // 管理员
            prodQueryVo.setUserId(null);
        } else {
            prodQueryVo.setUserId(getCurrentUserId());
        }
        Page<Prod> prods = prodService.queryProductionsByPage(new Page<>(page, limit), prodQueryVo);
        return DataResult.success(prods);
    }

    /**
     * 添加商品
     * @return
     */
    @RequestMapping("/add")
    public DataResult addProd(@RequestBody ProdUploadVo prodUploadVo) {
        Prod prod = new Prod();
        prod.setUserId(getCurrentUserId());
        BeanUtils.copyProperties(prodUploadVo, prod);
        prod = prodService.insertProd(prod);
        // 地址
        addrProdService.save(new AddrProd(prod.getProdId(), prodUploadVo.getAddress()));
        return DataResult.success(prod);
    }

    @RequestMapping("/del/batch")
    public DataResult delBatch(@RequestParam("ids") ArrayList<Integer> ids) {
        prodService.delBatch(ids);
        return DataResult.success(ids);
    }

    @GetMapping("/query/{id}")
    public DataResult getProdById(@PathVariable Long id) {
        Prod prod = prodService.getProdById(id);
        return DataResult.success(prod);
    }

    @PostMapping("update/id")
    public DataResult updateById(@RequestBody ProdUploadVo prodUploadVo) {
        Prod prod = new Prod();
        BeanUtils.copyProperties(prodUploadVo, prod);
        prodService.updateById(prod);
        return DataResult.success();
    }

    @GetMapping("/news/{limit}")
    public DataResult getNewsProd(@PathVariable Integer limit) {
        List<Prod> res = prodService.selectRecentProd(limit);
        return DataResult.success(res);
    }

    @GetMapping("/hot/{limit}")
    public DataResult getHotProd(@PathVariable Integer limit) {
        List<Prod> res = prodService.selectHotProd(limit);
        return DataResult.success(res);
    }

    @GetMapping("/search/{keyword}")
    public DataResult search(@PathVariable String keyword) {
        // TODO ES advance search
        List<Prod> res = prodService.search(keyword);
        return DataResult.success(res);
    }

    @GetMapping("/search/page/{keyword}")
    public DataResult searchPage(@PathVariable String keyword,Integer page, Integer limit) {
        // TODO ES advance search
        Page<Prod> res = prodService.searchByPage(keyword, new Page<>(page, limit));
        return DataResult.success(res);
    }



    @GetMapping("/sort/{key}/{limit}")
    public DataResult sortSearch(@PathVariable Integer limit, @PathVariable String key) {
        List<Prod> res = prodService.sortSearchByKey(key, limit);
        return DataResult.success(res);
    }

    @GetMapping("/recommend/{limit}")
    public DataResult getRecommendProd(@PathVariable Integer limit) {

        HttpServletRequest request = getRequest();
        HttpSession session = request.getSession();

        Object recommendProd = session.getAttribute("recommendProd");
        if (recommendProd == null) {
            recommendProd = prodService.getRecommendProd(limit);
            session.setAttribute("recommendProd", recommendProd);
        }

        List<Prod> recommendProdList = (List<Prod>) recommendProd;

        List<Prod> prods = RandomUtil.randomEleList(recommendProdList, limit);

        return DataResult.success(prods);
    }
}

