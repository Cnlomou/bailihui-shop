package com.bailihui.shop.controller;

import com.bailihui.shop.config.Constant;
import com.bailihui.shop.dto.Result;
import com.bailihui.shop.pojo.TbAdmin;
import com.bailihui.shop.pojo.TbUser;
import com.bailihui.shop.service.LoginService;
import com.bailihui.shop.util.Asserts;
import com.bailihui.shop.util.BCrypt;
import com.bailihui.shop.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Cnlomou
 * @create 2020/5/26 8:34
 */
@RestController
@Slf4j
@CrossOrigin
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login/{code}")
    public Result<TbUser> oauth2Login(@PathVariable(name = "code") String code, HttpServletResponse response) {
        log.info("请求登陆，code:" + code);
        TbUser login = loginService.login(code, response);
//        TbUser tbUser = new TbUser();
//        tbUser.setId(1);
//        tbUser.setUserid(code);
//        tbUser.setAddress("共青");
//        response.setHeader(Constant.jwt_head, JwtUtil.buildJwt("userinfo", tbUser));
        if (login == null)
            return new Result<>(false, "授权错误", null);
        return new Result<>(true, "SUCCESS", login);
    }

    @PostMapping("/admin/login")
    public Result adminLogin(@RequestBody  TbAdmin tbAdmin, HttpServletResponse response) {
        Asserts.check(loginService.adminLogin(tbAdmin.getAccount(), tbAdmin.getPasswd(), response),
                "密码错误");
        return Result.success();
    }

    @GetMapping("/logout")
    public Result adminLogOut(HttpServletRequest request, HttpServletResponse response) {
        if (loginService.adminLogOut(request, response)) {
            return Result.success();
        }
        return Result.failure("没有登陆");
    }

    public static void main(String[] args) throws Exception {
        System.out.println(BCrypt.checkpw("123456", "$2y$10$QEwHGQa/ZchrVBuuEMWD9..i7.HbLLEdjMDx6KIy44/2wFFkDyd4."));
//        System.out.println(BCrypt.hashpw("123456", BCrypt.gensalt()));
//        TbUser tbUser = new TbUser();
//        tbUser.setId(1);
//        tbUser.setUserid("sadfasdf");
//        tbUser.setAddress("共青");
//        System.out.println(JwtUtil.buildJwt("userinfo", tbUser));
//        System.out.println(JwtUtil.parser("eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1OTA1MDA0ODEsImp0aSI6IjMzNmI4ZWZhLWZiMjYtNDVkMi05N2MxLTU1ZDJjODRkZWRhNiIsInN1YiI6InVzZXJpbmZvIiwiaXNzIjoiTGlubW8iLCJUYlVzZXIiOnsiaWQiOjEsInVzZXJpZCI6InNhZGZhc2RmIiwiYWRkcmVzcyI6IuWFsemdkiIsImNyZWF0ZXRpbWUiOm51bGx9fQ.ZSclUpIOpV81rzX8YxsVT7YLazcWo_xdUU8YJcu_0Ew"));
    }
}
