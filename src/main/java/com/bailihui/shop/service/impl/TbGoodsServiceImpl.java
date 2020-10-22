package com.bailihui.shop.service.impl;

import com.bailihui.shop.config.Constant;
import com.bailihui.shop.dto.Goods;
import com.bailihui.shop.dto.GoodsDetails;
import com.bailihui.shop.mapper.TbCategoryMapper;
import com.bailihui.shop.pojo.TbCategory;
import com.bailihui.shop.pojo.TbGoods;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import com.bailihui.shop.mapper.TbGoodsMapper;
import com.bailihui.shop.service.TbGoodsService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Assert;
import tk.mybatis.mapper.util.Sqls;

import java.util.*;

/**
 * @author Cnlomou
 * @create 2020/5/25 20:22
 */
@Service
@Slf4j
public class TbGoodsServiceImpl implements TbGoodsService {
    private static final String[] SUPPORT_FIELD = {
            "price", "oldprice", "name", "status", "sellcount", "rating"
    };
    @Resource
    private TbGoodsMapper tbGoodsMapper;
    @Resource
    private TbCategoryMapper tbCategoryMapper;

    @Autowired
    private CacheManager cacheManager;

    @Override
    @Cacheable(cacheNames = {"goods"}, key = "'init'")
    public List<Goods> getGoods(int size) {
        log.info("读取了数据库-------------------");
        List<Goods> goods = new ArrayList<>();
        List<TbCategory> tbCategories = getCategories();
        tbCategories.forEach(category -> {
            goods.add(Goods.builder()
                    .category(category)
                    .goods(getGoods(category.getId(), size))
                    .build());
        });
        return goods;
    }

    private List<TbCategory> getCategories() {
        Cache cache = cacheManager.getCache("goods");
        List<TbCategory> tbCategories = null;
        Cache.ValueWrapper category = cache.get("category");
        if (category==null) {
            tbCategories = tbCategoryMapper.selectByExample(
                    Example.builder(TbCategory.class)
                            .orderByAsc("order")
                            .build());
        } else {
            log.info("读了cache中的分类信息");
            tbCategories = (List<TbCategory>) category.get();
        }
        return tbCategories;
    }

    private List<TbGoods> getGoods(int categoryId, int size) {
        PageHelper.startPage(0, size);
        Example sellcount = Example.builder(TbGoods.class)
                .orderByDesc("sellcount")
                .where(Sqls.custom()
                        .andEqualTo("categoryid", categoryId)
                        .andEqualTo("status", "1"))
                .build();
        return tbGoodsMapper.selectByExample(sellcount);
    }

    /**
     * 根据类型id查找对应的商品数据
     *
     * @param id
     * @return
     */
    @Override
    public Goods getGoodsByCategory(int id) {
        TbCategory tbCategory1 = tbCategoryMapper.selectByPrimaryKey(id);
        Assert.notNull(tbCategory1, "没有对应的类型，ID:" + id);
        Example sellcount = Example.builder(TbGoods.class)
                .orderByDesc("sellcount")
                .where(Sqls.custom()
                        .andEqualTo("categoryid", id)
                        .andEqualTo("status", "1"))
                .build();
        return Goods.builder()
                .category(tbCategory1)
                .goods(tbGoodsMapper.selectByExample(sellcount))
                .build();
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "goods", key = "init")
    public void insertGoods(List<TbGoods> goods) {
        goods.forEach(good -> {
            if (good.getOldprice() == null)
                good.setOldprice(good.getPrice());
            good.setIcon(good.getImage());
            good.setCreatetime(new Date());
            tbGoodsMapper.insertSelective(good);
        });
    }

    @Override
    public void updateGoods(TbGoods tbGoods) {
        Assert.notNull(tbGoods.getId(), "商品id不能为空");
        tbGoods.setUpdatetime(new Date());
        tbGoodsMapper.updateByPrimaryKeySelective(tbGoods);
    }

    @Override
    public void deleteGoodsById(Integer id) {
        tbGoodsMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void deleteAll(List<Integer> ids) {
        for (Integer id : ids) {
            this.deleteGoodsById(id);
        }
    }

    /**
     * 分页获取所有的商品信息，后台接口
     *
     * @param pageNum
     * @param size
     * @return
     */
    @Override
    public PageInfo<GoodsDetails> getGoodsByPage(int pageNum, int size) {
        Map<Integer, TbCategory> cache = new HashMap<>();
        PageHelper.startPage(pageNum, size);
        List<TbGoods> tbGoods = tbGoodsMapper.selectAll();
        Page<GoodsDetails> goodsList = new Page<>();
        transForm(goodsList, tbGoods, cache);
        if (tbGoods instanceof Page) {
            Page page = (Page) tbGoods;
            goodsList.setPageNum(page.getPageNum());
            goodsList.setPageSize(page.getPageSize());
            goodsList.setPages(page.getPages());
            goodsList.setStartRow(page.getStartRow());
            goodsList.setTotal(page.getTotal());
        }
        return PageInfo.of(goodsList);
    }

    /**
     * 根据字段修改商品信息
     *
     * @param id
     * @param field
     * @param value
     */
    @Override
    public void updateGoodsByField(Integer id, String field, String value) {
        int index = 0;
        if (!StringUtils.isEmpty(field) && (index = indexSupport(field)) < 0)
            throw new UnsupportedOperationException("不支持的字段：" + field);
        TbGoods tbGoods1 = getGoodsAndSetValue(id, index, value);
        log.info("更新商品信息,id:{},field:{}=>{}", id, field, value);
        tbGoodsMapper.updateByPrimaryKeySelective(tbGoods1);
    }

    @Override
    public Map<String, ?> readConfig() {
        Map<String, Double> map = new HashMap<>();
        map.put("minPrice", Constant.minPrice);
        map.put("deliveryPrice", Constant.deliveryPrice);
        return map;
    }


    private TbGoods getGoodsAndSetValue(Integer id, Integer fieldIndex, String value) {
        TbGoods goods = new TbGoods();
        goods.setId(id);
        switch (fieldIndex) {
            case 0:
                goods.setPrice(Double.valueOf(value));
                break;
            case 1:
                goods.setOldprice(Double.valueOf(value));
                break;
            case 2:
                goods.setName(value);
                break;
            case 3:
                TbGoods goods1 = tbGoodsMapper.selectByPrimaryKey(id);
                goods.setStatus(goods1.getStatus().equals("1") ? "0" : "1");
                break;
            case 4:
                goods.setSellcount(Integer.valueOf(value));
                break;
            case 5:
                goods.setRating(Integer.valueOf(value));
                break;
        }
        goods.setUpdatetime(new Date());
        return goods;
    }

    private int indexSupport(String field) {
        for (int i = 0; i < SUPPORT_FIELD.length; i++) {
            if (field.equals(SUPPORT_FIELD[i])) return i;
        }
        return -1;
    }

    private void transForm(List<GoodsDetails> goodsList, List<TbGoods> tbGoods, Map<Integer, TbCategory> cache) {
        tbGoods.forEach(goods -> {
            TbCategory category = getCategoryIfHas(cache, goods);
            goodsList.add(new GoodsDetails(category, goods));
        });
    }

    private TbCategory getCategoryIfHas(Map<Integer, TbCategory> cache, TbGoods goods) {
        TbCategory tbCategory = null;
        if ((tbCategory = cache.get(goods.getCategoryid())) == null) {
            tbCategory = tbCategoryMapper.selectByPrimaryKey(goods.getCategoryid());
            cache.put(tbCategory.getId(), tbCategory);
        }
        return tbCategory;
    }
}
