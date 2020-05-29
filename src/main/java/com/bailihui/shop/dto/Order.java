package com.bailihui.shop.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author Cnlomou
 * @create 2020/5/25 21:46
 */
@Data
@Builder
public class Order {
    Integer id;
    List<Goods> goodsList;
    Double money;
    Double deliveryPrice;
    Date time;
    String address;
    String userName;
    String status;
    @Data
    @Builder
    public static class Goods {
        String name;
        Double price;
        Integer count;
        Integer id;
    }
}
