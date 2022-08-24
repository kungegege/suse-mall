package com.frame.business.controller;


import com.frame.business.entity.Message;
import com.frame.business.service.MessageService;
import com.frame.business.service.SensitivewordFilter;
import com.frame.utils.DataResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 评论信息 前端控制器
 * </p>
 *
 * @author 邹坤
 * @since 2022-03-03
 */
@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private SensitivewordFilter sensitivewordFilter;

    @GetMapping("/get/all/{id}")
    public DataResult<Void> getMessageByTid(@PathVariable String id) {
        List<Message> msgs = messageService.queryAllByTid(id);
        return DataResult.success(msgs);
    }

    @PostMapping("/add")
    public DataResult<Void> getMessageByTid(@RequestBody Message message) {
        String msg = sensitivewordFilter.replace(message.getContent(), '*');
        message.setContent(msg);
        Message res = messageService.insert(message);
        return DataResult.success(res);
    }




}

