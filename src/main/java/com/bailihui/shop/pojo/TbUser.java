package com.bailihui.shop.pojo;

import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * @author Cnlomou
 * @create 2020/5/25 20:22
 */
@Data
@Table(name = "tb_user")
public class TbUser {
    @Id
    @Column(name = "id")
    private Integer id;

    /**
     * 用户标识
     */
    @Column(name = "userId")
    private String userid;

    /**
     * 收货地址
     */
    @Column(name = "address")
    private String address;

    /**
     * 创建时间
     */
    @Column(name = "createTime")
    private Date createtime;
}