package com.bailihui.shop.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Cnlomou
 * @create 2020/5/27 10:01
 */
public interface ImageService {
    List<String> putImage(MultipartFile[] multipartFile);
    void getImage(HttpServletResponse response, String name);
}
