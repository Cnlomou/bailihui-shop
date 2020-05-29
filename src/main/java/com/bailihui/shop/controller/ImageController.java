package com.bailihui.shop.controller;

import com.bailihui.shop.dto.Result;
import com.bailihui.shop.service.ImageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author Cnlomou
 * @create 2020/5/27 10:01
 */

@Controller
public class ImageController {
    @Resource
    ImageService imageService;

    @PostMapping("/upload")
    @ResponseBody
    public Result putImage(@RequestParam(name = "images") MultipartFile[] multipartFile) throws IOException {
        List<String> strings = imageService.putImage(multipartFile);
        return new Result<>(true,"SUCCESS",strings);
    }

    @GetMapping("/image/**")
    public void getImage(HttpServletRequest request,
                         HttpServletResponse response) throws IOException, ServletException {
        String servletPath1 = request.getServletPath();
        response.setHeader("Accept-Ranges","bytes");
        response.setContentType("image/*");
        response.setIntHeader("Expires",60*60*1000);       //设置一分钟的缓存
        response.setHeader("Cache-Control", "max-age=600");
        String str = "/bailihui";
        imageService.getImage(response,servletPath1.substring(servletPath1.indexOf(str)+str.length()));
    }
}
