package com.bailihui.shop.dto;

import com.bailihui.shop.pojo.TbCategory;
import com.bailihui.shop.pojo.TbGoods;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author Cnlomou
 * @create 2020/5/25 20:26
 */
@Data
@Builder
public class Goods {
    private TbCategory category;
    private List<TbGoods> goods;
}
