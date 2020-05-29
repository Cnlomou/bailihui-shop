package com.bailihui.shop.config;

import com.alibaba.fastjson.JSON;
import com.bailihui.shop.dto.Result;
import com.bailihui.shop.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @author Cnlomou
 * @create 2020/5/29 10:17
 */
@Slf4j
public class PermissionInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getCookies() != null)
            System.out.println(Arrays.toString(request.getCookies()));
        if (checkCookie(request))
            return true;
        else if (checkHeader(request))
            return true;
        if (log.isWarnEnabled())
            log.warn("没有权限的请求");
        response.setStatus(401);
        Result res = Result.failure(401, "请登录");
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JSON.toJSONString(res));
        return false;
    }

    private boolean checkHeader(HttpServletRequest request) {
        String jwt = request.getHeader(Constant.jwt_head);
        if (!StringUtils.isEmpty(jwt)) {
            try {
                String subject = JwtUtil.getSubject(jwt);
                if (!StringUtils.isEmpty(subject) && subject.equals(Constant.admin_subject))
                    return true;
            } catch (ExpiredJwtException e) {
                log.warn("用户令牌过期");
//                    throw new IllegalStateException("登陆过期，请重新登陆");
            } catch (Exception e) {
                log.error("解析令牌发生错误: " + e.getMessage());
                e.printStackTrace();
//                    throw new NotPermissionException("权限不足", e);
            }
        }
        return false;
    }

    private boolean checkCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) return false;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(Constant.jwt_head)) {
                String jwt = cookie.getValue();
                if (!StringUtils.isEmpty(jwt)) {
                    try {
                        String subject = JwtUtil.getSubject(jwt);
                        if (!StringUtils.isEmpty(subject) && subject.equals(Constant.admin_subject))
                            return true;
                    } catch (ExpiredJwtException e) {
                        log.warn("用户令牌过期");
                    } catch (Exception e) {
                        log.error("解析令牌发生错误: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }
}
