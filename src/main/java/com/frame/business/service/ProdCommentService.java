package com.frame.business.service;

import com.frame.business.entity.ProdComment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 商品评论表 服务类
 * </p>
 *
 * @author 邹坤
 * @since 2022-05-25
 */
public interface ProdCommentService extends IService<ProdComment> {

    List<ProdComment> queryByProdId(Long pid);
}
