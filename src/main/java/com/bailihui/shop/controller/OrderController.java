package com.bailihui.shop.controller;

import com.bailihui.shop.config.Constant;
import com.bailihui.shop.dto.Order;
import com.bailihui.shop.dto.Result;
import com.bailihui.shop.service.TbOrderService;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author Cnlomou
 * @create 2020/5/25 21:45
 */
@RestController
@RequestMapping("/order")
@CrossOrigin
@Slf4j
public class OrderController {

    @Autowired
    private TbOrderService tbOrderService;

    @GetMapping("/{id}")
    public Result<Order> findOrder(@PathVariable(name = "id") Integer id) {
        Order order = tbOrderService.selectOrderById(id);
        return new Result<>(true, "SUCCESS", order);
    }

    @GetMapping("/page/{pageNum}")
    public Result<PageInfo<Order>> findOrders(@PathVariable(name = "pageNum") Integer num) {
        PageInfo<Order> orderPageInfo = tbOrderService.selectOrderByPage(num);
        return new Result<>(true, "SUCCESS", orderPageInfo);
    }

    @PutMapping("/update/{id}/{isBuy}")
    public Result updateOrder(@PathVariable(name = "id") Integer id,
                              @PathVariable(name = "isBuy") Integer isBuy) {
        tbOrderService.updateStatus(id, isBuy > 0);
        return Result.SUCC;
    }

    @DeleteMapping("/{id}")
    public Result deleteOrder(@PathVariable(name = "id") Integer id) {
        tbOrderService.deleteOrderById(id);
        return Result.SUCC;
    }

    @DeleteMapping("/count")
    public Result deleteOrders(@RequestBody List<Integer> ids) {
        tbOrderService.deleteOrders(ids);
        return Result.SUCC;
    }

    @PostMapping("/add")
    public Result insertOrder(@RequestBody Order order, HttpServletRequest request) {
        log.info("订单信息:"+order.toString());
        tbOrderService.insertOrder(order, request);
        return Result.SUCC;
    }
}
