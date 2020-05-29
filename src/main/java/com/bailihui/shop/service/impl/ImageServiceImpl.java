package com.bailihui.shop.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bailihui.shop.dto.Result;
import com.bailihui.shop.service.ImageService;
import com.bailihui.shop.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * @author Cnlomou
 * @create 2020/5/27 10:01
 */

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    private static final String URL_PRE = "http://112.124.17.192:8989";
    private static final String[] SUFF = {".jpg", ".png", ".jpeg"};
    @Value("${image.path}")
    private String path;
    private Random random = new Random();

    @Override
    public List<String> putImage(MultipartFile[] multipartFile) {
        List<String> result = new ArrayList<>();
        for (MultipartFile file : multipartFile) {
            if (!isSupport(file))
                throw new UnsupportedOperationException("不支持的格式：" + file.getOriginalFilename());
            Response response = null;
            try {
                response = HttpUtil.uploadFile(URL_PRE + "/upload", "images", file);
                Result result1 = JSON.parseObject(response.body().string(), Result.class);
                if (result1.isFlag())
                    result.add((String) ((JSONArray) result1.getData()).get(0));
            } catch (IOException e) {
                log.error("上传文件失败，文件名:" + file.getOriginalFilename());
                throw new IllegalStateException("上传文件失败", e);
            } finally {
                if (response != null) {
                    response.close();
                }
            }
        }
        return result;
    }

    private File generateFile(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        StringBuffer buffer = new StringBuffer(path);
        buffer.append(File.separatorChar).append(getTimeString());
        File dir = new File(buffer.toString());
        dir.mkdirs();
        return new File(dir, File.separatorChar + Math.abs(random.nextInt()) +
                originalFilename.substring(originalFilename.lastIndexOf('.')));
    }

    private StringBuffer getTimeString() {
        StringBuffer buffer = new StringBuffer();
        Calendar instance = Calendar.getInstance();
        buffer.append(instance.get(Calendar.YEAR));
        int i = instance.get(Calendar.MONTH) + 1;
        int i1 = instance.get(Calendar.DAY_OF_MONTH);
        if (i > 9)
            buffer.append(i);
        else
            buffer.append('0').append(i);
        if (i1 > 9)
            buffer.append(i1);
        else
            buffer.append('0').append(i1);
        return buffer;

    }

    private boolean isSupport(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        for (String s : SUFF) {
            if (originalFilename.endsWith(s)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void getImage(HttpServletResponse response, String name) {
        try (
                Response response1 = HttpUtil.downLoadFile(URL_PRE + name);
        ) {
            log.info(name);
            InputStream inputStream1 = response1.body().byteStream();
            ServletOutputStream outputStream = response.getOutputStream();
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = inputStream1.read(bytes)) != -1)
                outputStream.write(bytes, 0, len);
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }
}
