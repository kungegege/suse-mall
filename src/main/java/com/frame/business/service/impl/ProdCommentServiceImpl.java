package com.frame.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.frame.business.entity.ProdComment;
import com.frame.business.entity.User;
import com.frame.business.mapper.ProdCommentMapper;
import com.frame.business.service.ProdCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.frame.business.service.UserService;
import com.frame.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 商品评论表 服务实现类
 * </p>
 *
 * @author 邹坤
 * @since 2022-05-25
 */
@Service
public class ProdCommentServiceImpl extends ServiceImpl<ProdCommentMapper, ProdComment> implements ProdCommentService {

    @Autowired
    private UserService userService;

    @Override
    public List<ProdComment> queryByProdId(Long pid) {
        QueryWrapper<ProdComment> prodCommentQueryWrapper = new QueryWrapper<>();
        prodCommentQueryWrapper.eq("prod_id", pid);
        List<ProdComment> prodComments = baseMapper.selectList(prodCommentQueryWrapper);
        for (ProdComment prodComment : prodComments) {
            User user = userService.getById(prodComment.getUserId());
            if (user != null) {
                user.setUserId(prodComment.getUserId());
                prodComment.setUserVo(UserVo.of(user));
            }
        }
        return prodComments;
    }


}
