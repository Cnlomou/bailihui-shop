package com.bailihui.shop.controller;

import com.bailihui.shop.config.Constant;
import com.bailihui.shop.dto.Goods;
import com.bailihui.shop.dto.GoodsDetails;
import com.bailihui.shop.dto.Result;
import com.bailihui.shop.pojo.TbGoods;
import com.bailihui.shop.service.TbGoodsService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Cnlomou
 * @create 2020/5/25 20:24
 */
@RestController
@RequestMapping("/goods")
@CrossOrigin
public class GoodsController {

    @Autowired
    TbGoodsService tbGoodsService;

    @GetMapping("/init/goods/{size}")
    public Result<List<Goods>> initGoods(@PathVariable(name = "size") Integer size) {
        List<Goods> goods = tbGoodsService.getGoods(size);
        return new Result<>(true, "SUCCESS", goods);
    }

    @GetMapping("/category/{id}")
    public Result<Goods> findByCategoryId(@PathVariable(name = "id") Integer id) {
        Goods goodsByCategory = tbGoodsService.getGoodsByCategory(id);
        return new Result<>(true, "SUCCESS", goodsByCategory);
    }

    @PutMapping("/update")
    public Result putGoods(TbGoods tbGoods) {
        tbGoodsService.updateGoods(tbGoods);
        return Result.SUCC;
    }

    @DeleteMapping("/{id}")
    public Result delByGoodsId(@PathVariable(name = "id") Integer id) {
        tbGoodsService.deleteGoodsById(id);
        return Result.SUCC;
    }

    @DeleteMapping("/all")
    public Result delAll(@RequestBody List<Integer> ids) {
        tbGoodsService.deleteAll(ids);
        return Result.success();
    }

    @PostMapping("/add")
    public Result addGoods(@RequestBody List<TbGoods> goods) {
        tbGoodsService.insertGoods(goods);
        return Result.SUCC;
    }

    @GetMapping("/config/get")
    public Result<Map<String, Double>> readConfig() {
        Map<String, Double> map = new HashMap<>();
        map.put("minPrice", Constant.minPrice);
        map.put("deliveryPrice", Constant.deliveryPrice);
        return new Result<>(true, "SUCCESS", map);
    }

    @GetMapping("/page/goodsdetails")
    public Result<PageInfo<GoodsDetails>> goodsDetails(Integer pageNum, Integer size) {
        PageInfo<GoodsDetails> goodsByPage = tbGoodsService.getGoodsByPage(pageNum, size);
        return new Result<>(true, "SUCCESS", goodsByPage);
    }

    @PutMapping("/update/{id}")
    public Result updateGoodsByField(@PathVariable(name = "id") Integer id, @RequestBody Map<String, String> param) {
        tbGoodsService.updateGoodsByField(id, param.get("field"), param.get("value"));
        return Result.SUCC;
    }
}
