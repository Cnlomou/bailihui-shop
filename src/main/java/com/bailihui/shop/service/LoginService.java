package com.bailihui.shop.service;

import com.bailihui.shop.pojo.TbUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Cnlomou
 * @create 2020/5/26 8:36
 */
public interface LoginService {
    TbUser login(String code, HttpServletResponse response);

    boolean adminLogin(String account, String password, HttpServletResponse response);

    boolean adminLogOut(HttpServletRequest request,HttpServletResponse response);
}
