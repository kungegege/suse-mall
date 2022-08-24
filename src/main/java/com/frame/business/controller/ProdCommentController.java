package com.frame.business.controller;


import com.frame.annotation.RequiresRoles;
import com.frame.business.base.BaseController;
import com.frame.business.entity.OrderProd;
import com.frame.business.entity.ProdComment;
import com.frame.business.entity.Role;
import com.frame.business.entity.UserRole;
import com.frame.business.service.OrderService;
import com.frame.business.service.ProdCommentService;
import com.frame.utils.DataResult;
import com.frame.vo.CommentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 商品评论 前端控制器
 * </p>
 *
 * @author 邹坤
 * @since 2022-05-25
 */
@RestController
@RequestMapping("/comment")
public class ProdCommentController extends BaseController {
    @Autowired
    private ProdCommentService prodCommentService;

    @Autowired
    private OrderService orderService;

    /**
     * 根据商品ID查询评论信息
     * @param pid
     * @return
     */
    @GetMapping("/query/prod/{pid}")
    public DataResult queryByProdId(@PathVariable Long pid) {
        List<ProdComment> result = prodCommentService.queryByProdId(pid);
        return DataResult.success(result);
    }

    /**
     * 添加评论
     * @param commentVo
     * @return
     */
    @PostMapping("add")
    @RequiresRoles
    public DataResult saveComment(@RequestBody CommentVo commentVo) {
        ProdComment comment = ProdComment.of(commentVo);
        comment.setUserId(getCurrentUserId());

        for (Long prodId : commentVo.getProdIds()) {
            // 商品订单中的所有商品添加评论
            comment.setId(null);
            comment.setProdId(prodId);
            replyComment(comment, "0");
        }
        return DataResult.success();
    }

    /**
     * 回复评论
     * @param prodComment
     * @param cid
     * @return
     */
    @PostMapping("reply/{cid}")
    @RequiresRoles
    public DataResult replyComment(@RequestBody ProdComment prodComment, @PathVariable String cid) {
        prodComment.setParentId(cid);
        prodComment.setCreateTime(LocalDateTime.now());
        prodComment.setBrowseTimes(0);
        prodComment.setUserId(getCurrentUserId());
        prodCommentService.save(prodComment);
        return DataResult.success(prodComment);
    }


    @GetMapping("/del/{id}")
    public DataResult delComment(@PathVariable String id){
        // 检查要删除的评论是否属于自己
        ProdComment prodComment = prodCommentService.getById(id);

        if (prodComment == null) {
            return DataResult.success();
        }

        if (prodComment.getUserId().equals(getCurrentUserId())
                || getCurrentRoles().contains(UserRole.MANAGE_ADMIN.getRoleName())) {
            prodCommentService.removeById(id);
        }
        return DataResult.success();
    }

}

