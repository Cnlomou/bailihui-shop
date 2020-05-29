package com.bailihui.shop.service.impl;

import com.alibaba.fastjson.JSON;
import com.bailihui.shop.config.Constant;
import com.bailihui.shop.mapper.TbAdminMapper;
import com.bailihui.shop.mapper.TbUserMapper;
import com.bailihui.shop.pojo.TbAdmin;
import com.bailihui.shop.pojo.TbUser;
import com.bailihui.shop.service.LoginService;
import com.bailihui.shop.util.Asserts;
import com.bailihui.shop.util.BCrypt;
import com.bailihui.shop.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Cnlomou
 * @create 2020/5/26 8:36
 */
@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

    private OkHttpClient client = new OkHttpClient();
    @Resource
    private TbUserMapper tbUserMapper;

    @Resource
    private TbAdminMapper tbAdminMapper;

    /**
     * 授权登陆
     *
     * @param code
     * @return
     */
    @Override
    public TbUser login(String code, HttpServletResponse response) {
        Map<String, String> userInfo = getUserInfo(code);
        String openid = userInfo.get("openid");
        if (!StringUtils.isEmpty(openid)) {
            TbUser tbUser = new TbUser();
            tbUser.setUserid(openid);
            List<TbUser> select = tbUserMapper.select(tbUser);
            if (select == null || select.size() == 0) {
                tbUser.setCreatetime(new Date());
                tbUserMapper.insertSelective(tbUser);
            }else
                tbUser = select.get(0);
            response.setHeader(Constant.jwt_head,JwtUtil.buildJwt("userinfo", tbUser));
            return tbUser;
        }
        return null;
    }

    @Override
    public boolean adminLogin(String account, String password, HttpServletResponse response) {
        List<TbAdmin> accountList = tbAdminMapper.selectByExample(Example.builder(TbAdmin.class)
                .where(Sqls.custom().andEqualTo("account", account))
                .build());
        Asserts.check(accountList.size() > 0, "账户名错误");
        TbAdmin tbAdmin = accountList.get(0);
        boolean res = false;
        if (res = BCrypt.checkpw(password, tbAdmin.getPasswd())) {
            tbAdmin.setPasswd(null);
            String jwt = JwtUtil.buildJwt(Constant.admin_subject, tbAdmin);
            Cookie cookie = new Cookie(Constant.jwt_head, jwt);
            cookie.setPath("/");
            cookie.setMaxAge(Constant.expire);
            response.addCookie(cookie);
        }
        return res;
    }

    /**
     * 退出登陆
     * @param request
     * @param response
     * @return  返回true 成功退出登陆
     */
    @Override
    public boolean adminLogOut(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) return false;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(Constant.jwt_head)) {
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
                return true;
            }
        }
        return false;
    }

    private Map<String, String> getUserInfo(String code) {
        Request build = new Request.Builder()
                .get()
                .url(buildUrl(code))
                .build();
        Response execute = null;
        try {
            execute = client.newCall(build).execute();
            return JSON.parseObject(execute.body().string(), Map.class);
        } catch (IOException e) {
            log.error("微信授权错误：" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (execute != null) {
                execute.close();
            }
        }
        return Collections.EMPTY_MAP;
    }

    private String buildUrl(String code) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(Constant.base_url).append('?')
                .append("appid=").append(Constant.appid).append('&')
                .append("secret=").append(Constant.secret).append('&')
                .append("grant_type=").append(Constant.grant_type).append('&')
                .append("js_code=").append(code);
        return buffer.toString();
    }
}
