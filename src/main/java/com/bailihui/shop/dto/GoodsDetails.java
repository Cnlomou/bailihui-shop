package com.bailihui.shop.dto;

/**
 * @author Cnlomou
 * @create 2020/5/27 20:49
 */

import com.bailihui.shop.pojo.TbCategory;
import com.bailihui.shop.pojo.TbGoods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品的详细信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsDetails {
    private TbCategory category;
    private TbGoods tbGoods;
}
