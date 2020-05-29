package com.bailihui.shop.service;

import com.bailihui.shop.dto.Order;
import com.bailihui.shop.util.Page;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author Cnlomou
 * @create 2020/5/25 20:22
 */
public interface TbOrderService{

    Order selectOrderById(Integer id);
    Page<Order> selectOrderByPage(Integer num);
    void deleteOrderById(Integer id);
    void deleteOrders(List<Integer> ids);
    void updateStatus(Integer id,boolean isBuy);
    void insertOrder(Order order, HttpServletRequest request);
}
