package com.bailihui.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author Cnlomou
 * @create 2020/5/25 20:14
 */
@SpringBootApplication
@MapperScan(basePackages = "com.bailihui.shop.mapper")
@EnableCaching
public class ShopApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(ShopApplication.class,args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ShopApplication.class);
    }
}
