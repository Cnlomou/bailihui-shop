package com.bailihui.shop.service.impl;

import com.alibaba.fastjson.JSON;
import com.bailihui.shop.config.Constant;
import com.bailihui.shop.dto.Order;
import com.bailihui.shop.exception.UnAuthorException;
import com.bailihui.shop.mapper.TbGoodsMapper;
import com.bailihui.shop.pojo.TbGoods;
import com.bailihui.shop.pojo.TbOrder;
import com.bailihui.shop.pojo.TbUser;
import com.bailihui.shop.service.TbGoodsService;
import com.bailihui.shop.util.JwtUtil;
import com.bailihui.shop.util.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwt;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.bailihui.shop.mapper.TbOrderMapper;
import com.bailihui.shop.service.TbOrderService;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Assert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Cnlomou
 * @create 2020/5/25 20:22
 */
@Service
@Slf4j
public class TbOrderServiceImpl implements TbOrderService {

    @Resource
    private TbOrderMapper tbOrderMapper;
    @Resource
    private TbGoodsMapper tbGoodsMapper;

    private static final String GOODS_PATTERN = "\\((\\d+)\\)\\{(\\d+)\\}";

    @Override
    public Order selectOrderById(Integer id) {
        TbOrder tbOrder = tbOrderMapper.selectByPrimaryKey(id);
        Order order = Order.builder()
                .deliveryPrice(tbOrder.getDeliveryprice())
                .id(tbOrder.getId())
                .time(tbOrder.getCreatetime())
                .money(tbOrder.getMoney())
                .goodsList(getGoodsList(tbOrder.getGoods()))
                .build();
        return order;
    }

    /**
     * 将goods字符串格式转化成对象
     * pattern:(1){2}
     *
     * @param goods
     * @return
     */
    private List<Order.Goods> getGoodsList(String goods) {
        Matcher matcher = Pattern.compile(GOODS_PATTERN).matcher(goods);
        List<Order.Goods> goodsList = new ArrayList<>();
        while (matcher.find()) {
            Integer id = Integer.valueOf(matcher.group(1));
            Integer count = Integer.valueOf(matcher.group(2));
            TbGoods tbGoods = tbGoodsMapper.selectByPrimaryKey(id);
            goodsList.add(Order.Goods.builder()
                    .id(id)
                    .name(tbGoods != null ? tbGoods.getName() : "已下架的商品")
                    .price(tbGoods != null ? tbGoods.getPrice() : 0)
                    .count(count)
                    .build());
        }
        return goodsList;
    }


    /**
     * 分页获取订单信息
     *
     * @param num 页数
     * @return
     */
    @Override
    public Page<Order> selectOrderByPage(Integer num) {
        PageHelper.startPage(num, Constant.order_page_size);
        com.github.pagehelper.Page<TbOrder> orders = (com.github.pagehelper.Page<TbOrder>) tbOrderMapper.selectByExample(Example.builder(TbOrder.class)
                .orderByDesc("createtime").build());
        List<Order> orderList = new ArrayList<>();
        orders.forEach(order -> {
            orderList.add(Order.builder()
                    .status(order.getStatus())
                    .userName(order.getUsername())
                    .time(order.getCreatetime())
                    .deliveryPrice(order.getDeliveryprice())
                    .id(order.getId())
                    .money(order.getMoney())
                    .goodsList(getGoodsList(order.getGoods()))
                    .address(order.getAddress())
                    .build());
        });
        return new Page<>(orders.getTotal(), num, orderList);
    }

    @Override
    public void deleteOrderById(Integer id) {
        tbOrderMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void deleteOrders(List<Integer> ids) {
        ids.forEach(id -> tbOrderMapper.deleteByPrimaryKey(id));
    }

    @Override
    public void updateStatus(Integer id, boolean isBuy) {
        char status = isBuy ? '1' : '0';
        TbOrder tbOrder = new TbOrder();
        tbOrder.setId(id);
        tbOrder.setStatus(Character.toString(status));
        tbOrderMapper.updateByPrimaryKeySelective(tbOrder);
    }

    @Override
    public void insertOrder(Order order, HttpServletRequest request) {
        TbUser user = getTbUser(request);
        TbOrder tbOrder = new TbOrder();
        tbOrder.setDeliveryprice(order.getDeliveryPrice());
        tbOrder.setCreatetime(new Date());
        tbOrder.setMoney(order.getMoney());
        tbOrder.setUserid(user.getUserid());
        tbOrder.setUsername(order.getUserName());
        tbOrder.setAddress(order.getAddress());
        tbOrder.setGoods(toPattern(order.getGoodsList()));
        tbOrderMapper.insertSelective(tbOrder);
        log.info("用户下单成功", order);
    }

    /**
     * 从jwt令牌中获取用户信息
     *
     * @param request
     * @return
     */
    @Nullable
    private TbUser getTbUser(HttpServletRequest request) {
        String header = request.getHeader(Constant.jwt_head);
        if (StringUtils.isEmpty(header))
            throw new UnAuthorException("没有登陆请不要添加订单");
        TbUser user = null;
        try {
            Jws<Claims> parser = JwtUtil.parser(header);
            String json = JSON.toJSONString(parser.getBody().get(TbUser.class.getSimpleName(), Map.class));
            user = JSON.parseObject(json, TbUser.class);
        } catch (Exception e) {
            log.error("解析jwt令牌错误:" + e.getMessage());
            e.printStackTrace();
            throw new UnAuthorException("解析jwt令牌错误", e);
        }
        return user;
    }

    private String toPattern(List<Order.Goods> goodsList) {
        StringBuffer buffer = new StringBuffer();
        goodsList.forEach(good -> {
            buffer.append('(').append(good.getId()).append(')')
                    .append('{').append(good.getCount()).append('}');
        });
        return buffer.toString();
    }
}
