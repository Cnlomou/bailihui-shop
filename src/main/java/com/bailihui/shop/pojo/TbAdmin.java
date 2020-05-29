package com.bailihui.shop.pojo;

import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * @author Cnlomou
 * @create 2020/5/28 19:53
 */
@Data
@Table(name = "tb_admin")
public class TbAdmin {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 名字
     */
    @Column(name = "`name`")
    private String name;

    /**
     * 账号
     */
    @Column(name = "account")
    private String account;

    /**
     * 邮箱
     */
    @Column(name = "email")
    private String email;

    /**
     * 密码
     */
    @Column(name = "passwd")
    private String passwd;

    /**
     * 0顶级，值越小权限越大
     */
    @Column(name = "permision")
    private Integer permision;

    /**
     * 是否锁定，1为锁定
     */
    @Column(name = "`lock`")
    private String lock;

    /**
     * 创建时间
     */
    @Column(name = "createTime")
    private Date createtime;
}