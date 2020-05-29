package com.bailihui.shop.pojo;

import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * @author Cnlomou
 * @create 2020/5/27 12:10
 */
@Data
@Table(name = "tb_order")
public class TbOrder {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 用户名
     */
    @Column(name = "userName")
    private String username;

    /**
     * 用户标识
     */
    @Column(name = "userId")
    private String userid;

    /**
     * 商品id，和数量,格式(1){1}
     */
    @Column(name = "goods")
    private String goods;

    /**
     * 消费总金额
     */
    @Column(name = "money")
    private Double money;

    /**
     * 下单时间
     */
    @Column(name = "createTime")
    private Date createtime;

    /**
     * 是否付款,0未付
     */
    @Column(name = "`status`")
    private String status;

    /**
     * 配送费
     */
    @Column(name = "deliveryPrice")
    private Double deliveryprice;

    /**
     * 送达地址
     */
    @Column(name = "address")
    private String address;
}