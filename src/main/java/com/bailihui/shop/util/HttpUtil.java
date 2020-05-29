package com.bailihui.shop.util;

import okhttp3.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * @author Cnlomou
 * @create 2020/5/29 21:13
 */
public class HttpUtil {
    public static final OkHttpClient CLIENT = new OkHttpClient();

    public static Response uploadFile(String url, String name, MultipartFile multipartFile) throws IOException {
        return uploadFile(url, name, multipartFile.getOriginalFilename(), multipartFile.getBytes());
    }

    public static Response uploadFile(String url, String name, String fileName, byte[] bytes) throws IOException {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(name, fileName, RequestBody.create(bytes, MediaType.parse("multipart/form-data")))
                .build();
        Request request = new Request.Builder()
                .post(requestBody)
                .url(url)
                .build();
        return CLIENT.newCall(request).execute();
    }

    public static Response uploadFile(String url, String name, File file) throws IOException {
        return uploadFile(url, name, file.getPath(), new FileInputStream(file));

    }

    public static Response uploadFile(String url, String name, String fileName, InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(bytes)) != -1)
            byteArrayOutputStream.write(bytes, 0, len);
        return uploadFile(url, name, fileName, byteArrayOutputStream.toByteArray());
    }

    public static Response downLoadFile(String url) throws IOException {
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        return CLIENT.newCall(request).execute();
    }
}

