package com.bailihui.shop.service.impl;

import com.bailihui.shop.pojo.TbCategory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.bailihui.shop.mapper.TbCategoryMapper;
import com.bailihui.shop.service.TbCategoryService;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Assert;

import java.util.List;

/**
 * @author Cnlomou
 * @create 2020/5/25 20:22
 */
@Service
public class TbCategoryServiceImpl implements TbCategoryService{

    @Resource
    private TbCategoryMapper tbCategoryMapper;

    @Override
    @CacheEvict(cacheNames = "goods",key = "'category'")
    public void insertCategory(TbCategory tbCategory) {
        tbCategoryMapper.insertSelective(tbCategory);
    }

    @Override
    public void updateCategory(TbCategory tbCategory) {
        Assert.notNull(tbCategory.getId(),"类型id不能为空");
        tbCategoryMapper.updateByPrimaryKey(tbCategory);
    }

    @Override
    public TbCategory selectCategoryById(Integer id) {
        return tbCategoryMapper.selectByPrimaryKey(id);
    }

    @Override
    public void deleteCategoryById(Integer id) {
        tbCategoryMapper.deleteByPrimaryKey(id);
    }

    @Override
    @Cacheable(cacheNames = "goods",key = "'category'")
    public List<TbCategory> selectAll() {
        return tbCategoryMapper.selectByExample(
                Example.builder(TbCategory.class)
                        .orderByAsc("order")
                        .build());
    }
}
