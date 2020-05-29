package com.bailihui.shop.service.impl;

import com.bailihui.shop.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * @author Cnlomou
 * @create 2020/5/27 10:01
 */

@Slf4j
public class ImageServiceImpl implements ImageService {

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
            try {
                File newFile = generateFile(file);
                file.transferTo(newFile);
                String path = newFile.getPath();
                path=path.replaceAll("\\\\","/");
                result.add(path.substring(this.path.length()));
            } catch (IOException e) {
                log.error(e.getMessage());
                e.printStackTrace();
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
        return new File(dir,File.separatorChar+Math.abs(random.nextInt())+
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
                OutputStream outputStream = response.getOutputStream();
                InputStream inputStream = new FileInputStream(path + name);
        ) {
            response.setContentLength(inputStream.available());
            byte[] bytes = new byte[1024];
            int res = 0;
            while ((res = inputStream.read(bytes)) != -1)
                outputStream.write(bytes, 0, res);
        } catch (FileNotFoundException e) {
            response.setStatus(404);
            log.warn("没有的图片文件：" + name);
            e.printStackTrace();
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }
}
