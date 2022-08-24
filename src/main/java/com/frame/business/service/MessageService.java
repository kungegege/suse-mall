package com.frame.business.service;

import com.frame.business.entity.Message;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 评论信息 服务类
 * </p>
 *
 * @author 邹坤
 * @since 2022-03-03
 */
public interface MessageService extends IService<Message> {

    List<Message> queryAllByTid(String id);

    Message insert(Message message);
}
