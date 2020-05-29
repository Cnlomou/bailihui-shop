package com.bailihui.shop.pojo;

import javax.persistence.*;
import lombok.Data;

/**
 * @author Cnlomou
 * @create 2020/5/25 20:22
 */
@Data
@Table(name = "tb_category")
public class TbCategory {
    /**
     * 主键
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 类型名
     */
    @Column(name = "`name`")
    private String name;

    /**
     * 类型
     */
    @Column(name = "`type`")
    private Short type;

    /**
     * 排序
     */
    @Column(name = "`order`")
    private Integer order;
}