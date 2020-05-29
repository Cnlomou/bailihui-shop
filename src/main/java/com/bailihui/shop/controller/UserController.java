package com.bailihui.shop.controller;

import com.bailihui.shop.dto.Result;
import com.bailihui.shop.service.TbUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author Cnlomou
 * @create 2020/5/25 23:40
 */
@RestController
@Slf4j
@CrossOrigin
public class UserController {
    @Autowired
    private TbUserService tbUserService;

    @PutMapping("/update/{id}")
    public Result updateAdress(@PathVariable(name = "id") Integer id, @RequestBody Map<String, String> params) {
        String addr = params.get("addr");
        log.info("用户{} 跟新地址信息为：{}", new Object[]{id, addr});
        if (!StringUtils.isEmpty(addr)) {
            tbUserService.updateAddress(id, addr.trim());
            return Result.SUCC;
        }
        return new Result<>(false, "无效的地址信息", null);
    }
}
