package com.frame.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.frame.business.entity.Message;
import com.frame.business.mapper.MessageMapper;
import com.frame.business.service.MessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 评论信息 服务实现类
 * </p>
 *
 * @author 邹坤
 * @since 2022-03-03
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Override
    public List<Message> queryAllByTid(String id) {
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        // 状态：0:未审核 1审核通过
        queryWrapper.eq("status", 1);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public Message insert(Message message) {
        baseMapper.insert(message);
        return message;
    }
}
