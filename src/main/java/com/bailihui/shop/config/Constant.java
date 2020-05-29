package com.bailihui.shop.config;

/**
 * @author Cnlomou
 * @create 2020/5/25 20:27
 */
public class Constant {
    //起送价格
    public static final double minPrice =20;
    //配送费
    public static final double deliveryPrice=4;

    public static final int order_page_size=20;

    public static final String user_attr="user";

    //oauth2
    public static final String base_url="https://api.weixin.qq.com/sns/jscode2session";
    public static final String appid="wxcd5a43c9bf3379f6";
    public static final String secret="29f141a4b02fd6a4405c39ba9165e840";
    public static final String grant_type="authorization_code";

    //jwt
    public static final String jwt_head="jwt";
    public static final String admin_subject="_adminInfo";

    //login expire
    public static final Integer expire=3*24*60*60;
}
