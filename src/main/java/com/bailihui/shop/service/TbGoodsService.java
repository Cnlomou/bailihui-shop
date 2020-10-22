package com.bailihui.shop.service;

import com.bailihui.shop.dto.Goods;
import com.bailihui.shop.dto.GoodsDetails;
import com.bailihui.shop.pojo.TbGoods;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * @author Cnlomou
 * @create 2020/5/25 20:22
 */
public interface TbGoodsService{
    List<Goods> getGoods(int size);
    Goods getGoodsByCategory(int id);
    void insertGoods(List<TbGoods> goods);
    void updateGoods(TbGoods tbGoods);
    void deleteGoodsById(Integer id);
    void deleteAll(List<Integer> ids);

    PageInfo<GoodsDetails> getGoodsByPage(int pageNum, int size);

    void updateGoodsByField(Integer id,String field,String value);

    Map<String,?> readConfig();
}
