package com.bailihui.shop.service;

import com.bailihui.shop.pojo.TbAdmin;
import com.bailihui.shop.util.Page;

/**
 * @author Cnlomou
 * @create 2020/5/28 9:50
 */
public interface TbAdminService{

    void insertAdmin(TbAdmin admin);

    TbAdmin selectAdminById(Integer id);

    Page<TbAdmin> selectAdminByPage(Integer pageNum);

    void lockAccount(Integer id,Integer destId);

    void updatePasswd(Integer id,String newPasswd);

    void deleteAccount(Integer id,Integer destId);
}
