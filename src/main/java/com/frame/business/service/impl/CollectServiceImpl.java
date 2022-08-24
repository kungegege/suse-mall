package com.frame.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.frame.business.entity.Collect;
import com.frame.business.mapper.CollectMapper;
import com.frame.business.service.CollectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 邹坤
 * @since 2022-05-07
 */
@Service
public class CollectServiceImpl extends ServiceImpl<CollectMapper, Collect> implements CollectService {

    @Override
    public List<Collect> queryByUid(String userId) {
        QueryWrapper<Collect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public void deleteByPid(String uid, Long pid) {
        QueryWrapper<Collect> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", uid).eq("prod_id", pid);
        baseMapper.delete(wrapper);
    }

    @Override
    public Collect saveAndUpdate(Long pid, String currentUserId) {
        QueryWrapper<Collect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("prod_id", pid)
                .eq("user_id", currentUserId);

        Collect collect = baseMapper.selectOne(queryWrapper);

        if (collect == null) {
            // 保存
            Collect col = new Collect(pid, currentUserId);
            this.save(col);
        } else {
            // 删除
            Long id = collect.getId();
            baseMapper.deleteById(id);
        }

        return collect;

    }

    @Override
    public Collect query(Long pid, String currentUserId) {
        QueryWrapper<Collect> collectQueryWrapper = new QueryWrapper<>();
        collectQueryWrapper.eq("prod_id", pid)
                .eq("user_id", currentUserId);
        return baseMapper.selectOne(collectQueryWrapper);
    }

}
