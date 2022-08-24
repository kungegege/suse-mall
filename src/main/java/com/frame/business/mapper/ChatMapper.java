package com.frame.business.mapper;

import com.frame.business.entity.Chat;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.frame.vo.ChatMessageVo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 邹坤
 * @since 2022-05-09
 */
public interface ChatMapper extends BaseMapper<Chat> {


    /**
     * 查询最新消息
     * @param uid
     * @param tid
     * @return
     */
    ChatMessageVo queryLastMsg(String uid, String tid);

}
