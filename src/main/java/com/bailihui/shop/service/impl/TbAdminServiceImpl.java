package com.bailihui.shop.service.impl;

import com.bailihui.shop.exception.NotPermissionException;
import com.bailihui.shop.pojo.TbAdmin;
import com.bailihui.shop.util.Asserts;
import com.bailihui.shop.util.BCrypt;
import com.bailihui.shop.util.Page;
import com.github.pagehelper.PageHelper;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import com.bailihui.shop.mapper.TbAdminMapper;
import com.bailihui.shop.service.TbAdminService;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Cnlomou
 * @create 2020/5/28 9:50
 */
@Service
@Slf4j
public class TbAdminServiceImpl implements TbAdminService {

    @Resource
    private TbAdminMapper tbAdminMapper;

    private static final int PAGE_SIZE = 10;

    @Override
    public void insertAdmin(TbAdmin admin) {
        admin.setCreatetime(new Date());
        admin.setPermision(1);
        String hashpw = BCrypt.hashpw(admin.getPasswd(), BCrypt.gensalt());
        admin.setPasswd(hashpw);
        tbAdminMapper.insertSelective(admin);
    }

    @Override
    public TbAdmin selectAdminById(Integer id) {
        TbAdmin tbAdmin = tbAdminMapper.selectByPrimaryKey(id);
        tbAdmin.setPasswd(null);
        return tbAdmin;
    }

    @Override
    public Page<TbAdmin> selectAdminByPage(Integer pageNum) {
        PageHelper.startPage(pageNum, PAGE_SIZE);
        com.github.pagehelper.Page<TbAdmin> tbAdmins = (com.github.pagehelper.Page<TbAdmin>) tbAdminMapper.selectAll();
        List<TbAdmin> admins = new ArrayList<>();
        tbAdmins.forEach(admin -> {
            admin.setPasswd(null);
            admins.add(admin);
        });
        return new Page<>(tbAdmins.getTotal(), pageNum, admins);
    }

    /**
     * 锁定账户
     *
     * @param id
     * @param destId 被锁定的账户
     */
    @Override
    public void lockAccount(Integer id, Integer destId) {
        TbAdmin tbAdmin = tbAdminMapper.selectByPrimaryKey(id);
        Assert.notNull(tbAdmin, "没有该id信息：" + id);
        Example example = Example.builder(TbAdmin.class)
                .where(Sqls.custom().andLessThan("permision", tbAdmin.getPermision()))
                .build();
        TbAdmin destAdmin = new TbAdmin();
        destAdmin.setId(destId);
        destAdmin.setLock(tbAdmin.getLock().equals("1") ? "0" : "1");
        log.info("账户：{}将被 {}修改锁定状态", destId, id);
        Asserts.checkPermission(tbAdminMapper.updateByExampleSelective(destAdmin, example) > 0,
                "权限不足的账户:" + id);

    }

    @Override
    public void updatePasswd(Integer id, String newPasswd) {
        Assert.isTrue(tbAdminMapper.existsWithPrimaryKey(id), "没有该id信息：" + id);
        TbAdmin newInfo = new TbAdmin();
        newInfo.setId(id);
        String hashpw = BCrypt.hashpw(newPasswd, BCrypt.gensalt());
        newInfo.setPasswd(hashpw);
        tbAdminMapper.updateByPrimaryKeySelective(newInfo);
    }

    @Override
    public void deleteAccount(Integer id, Integer destId) {
        TbAdmin tbAdmin = tbAdminMapper.selectByPrimaryKey(id);
        Assert.notNull(tbAdmin, "没有该id信息：" + id);
        Example example = Example.builder(TbAdmin.class)
                .where(Sqls.custom()
                        .andLessThan("permision", tbAdmin.getPermision())
                        .andEqualTo("id", destId))
                .build();
        tbAdminMapper.deleteByExample(example);
        Asserts.checkPermission(tbAdminMapper.deleteByExample(example) > 0,
                "权限不足的账户:" + id);
    }
}
