package com.frame.business.service;

import com.frame.business.entity.AddrProd;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 邹坤
 * @since 2022-04-26
 */
public interface AddrProdService extends IService<AddrProd> {

    List<AddrProd> queryByPid(Long id);
}
