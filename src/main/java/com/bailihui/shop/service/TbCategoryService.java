package com.bailihui.shop.service;

import com.bailihui.shop.pojo.TbCategory;

import java.util.List;

/**
 * @author Cnlomou
 * @create 2020/5/25 20:22
 */
public interface TbCategoryService{

    void insertCategory(TbCategory tbCategory);
    void updateCategory(TbCategory tbCategory);
    TbCategory selectCategoryById(Integer id);
    void deleteCategoryById(Integer id);
    List<TbCategory> selectAll();
}
