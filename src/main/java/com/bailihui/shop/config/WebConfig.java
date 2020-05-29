package com.bailihui.shop.config;

import com.alibaba.fastjson.JSON;
import com.bailihui.shop.dto.Result;
import com.bailihui.shop.util.JwtUtil;
import com.bailihui.shop.util.Util;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.w3c.dom.ls.LSInput;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author Cnlomou
 * @create 2020/5/27 15:56
 */

@Configuration
@Slf4j
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowCredentials(true);

//                .allowedMethods("GET", "POST", "OPTIONS", "PUT")
//                .allowedHeaders("Content-Type", "X-Requested-With", "accept", "Origin", "Access-Control-Request-Method",
//                        "Access-Control-Request-Headers")
//                .exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials")
//                .allowCredentials(true).maxAge(3600);

    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new PermissionInterceptor())
                .addPathPatterns("/admin/**").excludePathPatterns("/admin/login")
                .addPathPatterns("/goods/**").excludePathPatterns("/goods/init/goods/*", "/goods/category/*", "/goods/config/get","/goods/page/goodsdetails")
                .addPathPatterns("/category/**").excludePathPatterns("/category/all", "/category/{id}")
                .addPathPatterns("/upload")
                .addPathPatterns("/order/**");
    }



    public FilterRegistrationBean filterRegistrationBean(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        List<Predicate<String>> strings = Arrays.asList(Util.matchSuf("/admin/login"),
                Util.matchPre("/bailihui/goods/init/goods"),
                Util.matchPre("/bailihui/goods/category"),
                Util.matchSuf("/config/get")
        );
        filterRegistrationBean.setFilter(new PermissionFilter(strings));
        filterRegistrationBean.setUrlPatterns(
                Arrays.asList("/bailihui/admin/**",
                        "/bailihui/upload",
                        "/bailihui/order/**",
                        "/bailihui/goods/**",
                        "/bailihui/category/"
                ));
        return filterRegistrationBean;
    }
}
