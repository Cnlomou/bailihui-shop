package com.bailihui.shop.controller;

import com.bailihui.shop.dto.Result;
import com.bailihui.shop.pojo.TbAdmin;
import com.bailihui.shop.service.TbAdminService;
import com.bailihui.shop.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


/**
 * @author Cnlomou
 * @create 2020/5/28 10:16
 */
@RestController
@RequestMapping("/admin")
@CrossOrigin
public class AdminController {

    @Autowired
    private TbAdminService tbAdminService;

    @PostMapping(value = "/add")
    public Result addAccount(@RequestBody TbAdmin admin) {
        tbAdminService.insertAdmin(admin);
        return Result.SUCC;
    }

    @GetMapping("/sel/{id}")
    public Result<TbAdmin> getAdminById(@PathVariable(name = "id") Integer id) {
        TbAdmin tbAdmin = tbAdminService.selectAdminById(id);
        return Result.success(tbAdmin);
    }

    @GetMapping("/page/{pageNum}")
    public Result<Page<TbAdmin>> adminByPage(@PathVariable(name = "pageNum") Integer pageNum){
        Page<TbAdmin> tbAdminPage = tbAdminService.selectAdminByPage(pageNum);
        return Result.success(tbAdminPage);
    }

    @PutMapping("/lock/{id}/{destId}")
    public Result lockAccount(@PathVariable(name = "id") Integer id,
                              @PathVariable(name = "destId") Integer desId) {
        tbAdminService.lockAccount(id, desId);
        return Result.success();
    }

    @PutMapping("/update")
    public Result updateAccount(@RequestBody TbAdmin tbAdmin) {
        tbAdminService.updatePasswd(tbAdmin.getId(), tbAdmin.getPasswd());
        return Result.success();
    }

    @DeleteMapping("/del/{id}/{destId}")
    public Result deleteAccount(@PathVariable(name = "id") Integer id,
                                @PathVariable(name = "destId") Integer destId) {
        tbAdminService.deleteAccount(id, destId);
        return Result.success();
    }
}
