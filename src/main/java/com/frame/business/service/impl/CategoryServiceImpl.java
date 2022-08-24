package com.frame.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.frame.business.entity.Category;
import com.frame.business.mapper.CategoryMapper;
import com.frame.business.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.frame.vo.CategoryQueryVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 产品类目 服务实现类
 * </p>
 *
 * @author 邹坤
 * @since 2022-03-14
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Override
    public List<Category> queryAll() {
        // shine
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        List<Category> categories = baseMapper.selectList(queryWrapper);
        return categories;
    }

    @Override
    public List<Integer> selectIdsAll(Integer id) {
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("category_id")
                .eq("status", 1)
                .eq("parent_id", id);

        List<Integer> ids = baseMapper.selectList(queryWrapper)
                .stream()
                .map(it -> it.getCategoryId().intValue())
                .collect(Collectors.toList());

        if (ids.isEmpty()) {
            return new ArrayList<>();
        }

        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        wrapper.select("category_id")
                .eq("status", 1)
                .in("category_id", ids)
                .or()
                .in("parent_id", ids);
        return baseMapper.selectList(wrapper).stream().map(w -> w.getCategoryId().intValue()).collect(Collectors.toList());
    }

    @Override
    public Page<Category> queryCategoryByPage(String typeName, Page<Category> page) {
        QueryWrapper<Category> categoryQueryWrapper = new QueryWrapper<>();
        categoryQueryWrapper.like(!StringUtils.isEmpty(typeName), "category_name", typeName)
                .eq("status", 1);
        return baseMapper.selectPage(page,categoryQueryWrapper);
    }

    @Override
    public Page<Category> queryCategoryByPage(CategoryQueryVo categoryQueryVo, Page<Category> page) {
        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        String categoryName = categoryQueryVo.getCategoryName();
        Long categoryId = categoryQueryVo.getCategoryId();
        wrapper.eq(categoryId != null, "category_id", categoryId)
                .like(!StringUtils.isEmpty(categoryName), "category_name", categoryName);
        return baseMapper.selectPage(page, wrapper);
    }

    @Override
    public List<Category> queryByName(String categoryName) {
        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        wrapper.like(!StringUtils.isEmpty(categoryName), "category_name", categoryName);
        wrapper.eq("status", 1);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public Category getCategoryById(Long id) {

        return baseMapper.selectById(id);
    }

    @Override
    public void deleteBatch(List<Long> ids) {
        baseMapper.deleteBatchIds(ids);
    }

    @Override
    public int getChildrenCount(Long id) {
        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",id);
        return baseMapper.selectCount(wrapper);
    }

    @Override
    public List<Category> findChildren(Long id) {

        ArrayList<Long> list = new ArrayList<>();
        list.add(id);

        ArrayList<Category> categories = new ArrayList<>();

        List<Category> allData = baseMapper.selectList(null);

        filterChildrenCategory(list, categories, allData);

        return categories;
    }

    @Override
    public Page<Category> queryCategoryByLevelPage(Integer level, Page<Category> page) {
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("grade", level);
        return baseMapper.selectPage(page, queryWrapper);
    }

    @Override
    public boolean checkExistByName(String categoryName) {
        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1);
        wrapper.eq("category_name", categoryName);
        return baseMapper.selectCount(wrapper) > 0;
    }

    private void filterChildrenCategory(List<Long> ids ,List<Category> container, List<Category> allData){

        List<Category> list = allData.stream().filter(it -> {
            return ids.contains(it.getParentId());
        }).collect(Collectors.toList());

        if (list.isEmpty()){
            return;
        }

        container.addAll(list);

        List<Long> cIds = list.stream().map(it -> it.getCategoryId()).collect(Collectors.toList());

        filterChildrenCategory(cIds, container, allData);
    }

}
