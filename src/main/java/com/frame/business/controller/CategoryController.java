package com.frame.business.controller;


import cn.hutool.core.lang.hash.Hash;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.frame.business.entity.Category;
import com.frame.business.service.CategoryService;
import com.frame.utils.DataResult;
import com.frame.vo.CategoryQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * @author: HP-zouKun
 * Date: 2022/5/1
 * Time: 20:42
 * Description: 分类模块
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("list")
    public DataResult getCategoryList() {
        List<Category> list = categoryService.queryAll();
        return DataResult.success(list);
    }
    @GetMapping("tree")
    public DataResult<List<Tree<Integer>>> getCategory() {
        List<Category> list = categoryService.queryAll();
        List<TreeNode<Integer>> treeNodeList = list.stream().map(category -> {
            Map<String, Object> map = new HashMap<>();
            map.put("pic", category.getPic());
            TreeNode<Integer> treeNode = new TreeNode<>();
            treeNode.setName(category.getCategoryName());
            treeNode.setId(category.getCategoryId().intValue());
            treeNode.setWeight(category.getSeq());
            treeNode.setExtra(map);
            treeNode.setParentId(category.getParentId().intValue());
            return treeNode;
        }).collect(Collectors.toList());
        List<Tree<Integer>> treeList = TreeUtil.build(treeNodeList);
        return DataResult.success(treeList);
    }

    /**
     * 查询树形结构的所有分类数据
     * @param id
     * @return
     */
    @GetMapping("tree/{id}")
    public DataResult getCategoryTreeById(@PathVariable Long id) {
        // 所有分类列表
        List<Category> list = categoryService.queryAll();

        List<Category> collect = list.stream()
                .filter(it -> it.getParentId().equals(id))
                .collect(Collectors.toList());
        List<Long> ids = collect.stream().map(it -> it.getCategoryId())
                .collect(Collectors.toList());
        List<Category> collect1 = list.stream()
                .filter(it -> ids.contains(it.getParentId()))
                .collect(Collectors.toList());
        ArrayList<Category> categories = new ArrayList<>();

        categories.addAll(collect1);
        categories.addAll(collect);

        List<TreeNode<Integer>> treeNodeList = categories.stream()
                .map(category -> {
            TreeNode<Integer> treeNode = new TreeNode<>();
            treeNode.setName(category.getCategoryName());
            treeNode.setId(category.getCategoryId().intValue());
            treeNode.setWeight(category.getSeq());
            treeNode.setParentId(category.getParentId().intValue());
            return treeNode;
        }).collect(Collectors.toList());

        List<Tree<Integer>> treeList = TreeUtil.build(treeNodeList, id.intValue());
        return DataResult.success(treeList);
    }


    @GetMapping("/all")
    public DataResult getCategoryByPage(String typeName, Integer page, Integer limit) {
        Page<Category> categoryPage = new Page<>(page, limit);

        Page<Category> res = categoryService.queryCategoryByPage(typeName, categoryPage);
        return DataResult.success(res);
    }

    @GetMapping("query/{id}")
    public DataResult<Category> getCategoryById(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);
        return DataResult.success(category);
    }

    /**
     * 分页查询
     * @param page  起始页
     * @param limit 页大小
     * @param categoryQueryVo
     * @return
     */
    @GetMapping("/page")
    public DataResult queryCategoryByPage(Integer page, Integer limit, CategoryQueryVo categoryQueryVo) {
        Page<Category> prods = categoryService.queryCategoryByPage(categoryQueryVo, new Page<>(page, limit));
        return DataResult.success(prods);
    }

    /**
     * 通过ID修改分类
     * @param category
     * @return
     */
    @PostMapping("/update/id")
    public DataResult updateById(@RequestBody Category category) {
        if (category.getParentId() == null) {
            category.setParentId(0L);
        }
        category.setUpdateTime(LocalDateTime.now());
        categoryService.updateById(category);
        return DataResult.success();
    }

    /**
     * 新建分类
     * @param category
     * @return
     */
    @PostMapping("/save")
    public DataResult save(@RequestBody Category category) {
        category.setRecTime(LocalDateTime.now());
        if (category.getParentId() == null) {
            category.setParentId(0L);
        }

        if (category.getSeq() == null) {
            category.setSeq(1);
        }

        if (categoryService.checkExistByName(category.getCategoryName())) {
            return DataResult.getResult(90001, "该分类已存在");
        }

        categoryService.save(category);
        return DataResult.success();
    }

    /**
     * 批量输出分类
     * @param ids
     * @return
     */
    @GetMapping("/delete/batch")
    public DataResult deleteBatch(@RequestParam("ids") ArrayList<Long> ids) {
        ArrayList<Category> res = new ArrayList<>();
        for (Long id : ids) {
            List<Category> categories = categoryService.findChildren(id);
            res.addAll(categories);
        }
        List<Long> cids = res.stream().map(it -> it.getCategoryId()).distinct().collect(Collectors.toList());
        cids.addAll(ids);

        categoryService.deleteBatch(cids);
        return DataResult.success();
    }

    @GetMapping("/level/{level}")
    public DataResult getCategoryByLevelPage(@PathVariable Integer level,Integer page, Integer limit) {
        Page<Category> res = categoryService.queryCategoryByLevelPage(level, new Page<Category>(page, limit));
        return DataResult.success(res);
    }

}