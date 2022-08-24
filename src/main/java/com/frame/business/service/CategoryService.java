package com.frame.business.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.frame.business.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import com.frame.vo.CategoryQueryVo;
import java.util.List;

/**
 * <p>
 * 产品类目 服务类
 * </p>
 *
 * @author 邹坤
 * @since 2022-03-14
 */
public interface CategoryService extends IService<Category> {

    List<Category> queryAll();

    List<Integer> selectIdsAll(Integer id);

    Page<Category> queryCategoryByPage(String typeName, Page<Category> page);

    Page<Category> queryCategoryByPage(CategoryQueryVo categoryQueryVo, Page<Category> page);

    /**
     * 根据名字查询
     * @param categoryName
     * @return
     */
    List<Category> queryByName(String categoryName);


    Category getCategoryById(Long id);

    void deleteBatch(List<Long> ids);

    int getChildrenCount(Long id);

    List<Category> findChildren(Long id);

    Page<Category> queryCategoryByLevelPage(Integer level, Page<Category> page);

     boolean checkExistByName(String categoryName);
}
