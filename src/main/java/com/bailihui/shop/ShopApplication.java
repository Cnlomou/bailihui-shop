package com.bailihui.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author Cnlomou
 * @create 2020/5/25 20:14
 */
@SpringBootApplication
@MapperScan(basePackages = "com.bailihui.shop.mapper")
public class ShopApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShopApplication.class,args);
    }

}
