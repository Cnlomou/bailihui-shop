package com.bailihui.shop.pojo;

import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * @author Cnlomou
 * @create 2020/5/25 21:11
 */
@Data
@Table(name = "tb_goods")
public class TbGoods {
    /**
     * 主键
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 商品名
     */
    @Column(name = "`name`")
    private String name;

    /**
     * 当前价格
     */
    @Column(name = "price")
    private Double price;

    /**
     * 旧价格
     */
    @Column(name = "oldPrice")
    private Double oldprice;

    /**
     * 描述
     */
    @Column(name = "description")
    private String description;

    /**
     * 当月销售计数
     */
    @Column(name = "sellCount")
    private Integer sellcount;

    /**
     * 总销售数
     */
    @Column(name = "`Count`")
    private Integer count;

    /**
     * 好评数
     */
    @Column(name = "rating")
    private Integer rating;

    /**
     * 信息
     */
    @Column(name = "info")
    private String info;

    /**
     * 图标
     */
    @Column(name = "icon")
    private String icon;

    /**
     * 1上架，0下架，默认0
     */
    @Column(name = "`status`")
    private String status;

    /**
     * 图片
     */
    @Column(name = "image")
    private String image;

    /**
     * 分类id
     */
    @Column(name = "categoryId")
    private Integer categoryid;

    /**
     * 上架时间
     */
    @Column(name = "createTime")
    private Date createtime;

    /**
     * 修改时间
     */
    @Column(name = "updateTime")
    private Date updatetime;
}