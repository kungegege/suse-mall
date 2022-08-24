package com.frame.business.service;

import com.frame.business.entity.Collect;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 邹坤
 * @since 2022-05-07
 */
public interface CollectService extends IService<Collect> {

    List<Collect> queryByUid(String userId);

    void deleteByPid(String uid, Long pid);

    Collect saveAndUpdate(Long pid, String currentUserId);

    Collect query(Long pid, String currentUserId);
}
