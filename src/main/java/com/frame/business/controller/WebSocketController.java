package com.frame.business.controller;

import com.frame.annotation.RequiresRoles;
import com.frame.business.base.BaseController;
import com.frame.contants.Constant;
import com.frame.utils.DataResult;
import com.frame.utils.RedisTemplateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/3/4
 * Time: 10:05
 * Description:
 */
@RestController
@RequestMapping("/socket")
@Slf4j
public class WebSocketController extends BaseController {

    @Autowired
    private RedisTemplateUtil redisTemplateUtil;


    /**
     * 查询自己的消息邮箱
     *
     * @param id 用户ID TODO 集成用户模块后，自动从session获取
     * @return
     */
    @GetMapping("/{id}")
    public DataResult<Void> selectMsg(@PathVariable String id) {
        // key sourceID， value：message count
        HashMap<String, Integer> res = new HashMap<>();
        String address = (String) redisTemplateUtil.hget(Constant.WebSocketConstant.CHAT_CLIENTS_HASH, id);
        log.info(" the message container address is {} ", address);

        if (address != null) {
            for (String s : address.split(",")) {
                Long count = redisTemplateUtil.llen(s);
                res.put(s.split("-")[1], count.intValue());
            }
        }
        return DataResult.success(res);
    }



}
