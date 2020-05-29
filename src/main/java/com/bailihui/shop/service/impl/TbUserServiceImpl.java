package com.bailihui.shop.service.impl;

import com.bailihui.shop.pojo.TbUser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import com.bailihui.shop.mapper.TbUserMapper;
import com.bailihui.shop.service.TbUserService;

/**
 * @author Cnlomou
 * @create 2020/5/25 20:22
 */
@Service
public class TbUserServiceImpl implements TbUserService {

    @Resource
    private TbUserMapper tbUserMapper;

    /**
     * 修改用户地址信息
     * @param id
     * @param address
     */
    @Override
    public void updateAddress(Integer id, String address) {
        TbUser tbUser = new TbUser();
        tbUser.setId(id);
        tbUser.setAddress(address);
        tbUserMapper.updateByPrimaryKeySelective(tbUser);
    }
}
