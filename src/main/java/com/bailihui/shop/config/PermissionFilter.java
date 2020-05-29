package com.bailihui.shop.config;

import com.alibaba.fastjson.JSON;
import com.bailihui.shop.dto.Result;
import com.bailihui.shop.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author Cnlomou
 * @create 2020/5/29 10:08
 */
@Slf4j
public class PermissionFilter implements Filter {
    private List<Predicate<String>> exclude = Collections.EMPTY_LIST;

    public PermissionFilter(List<Predicate<String>> exclude) {
        this.exclude = exclude;
    }

    public PermissionFilter() {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest) {
            HttpServletRequest servletRequest1 = (HttpServletRequest) servletRequest;
            HttpServletResponse servletResponse1 = (HttpServletResponse) servletResponse;
            if (!doExclude(servletRequest1, filterChain))
                intercepte(servletRequest1, servletResponse1, filterChain);
        }
    }

    private void intercepte(HttpServletRequest servletRequest, HttpServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Cookie[] cookies = servletRequest.getCookies();
        if (cookies != null || cookies.length > 0) {
            if (servletRequest.getCookies() != null)
                System.out.println(Arrays.toString(servletRequest.getCookies()));
            if (checkCookie(servletRequest))
                filterChain.doFilter(servletRequest, servletResponse);
            else if (checkHeader(servletRequest))
                filterChain.doFilter(servletRequest, servletResponse);
            if (log.isWarnEnabled())
                log.warn("没有权限的请求");
            servletResponse.setStatus(401);
            Result res = Result.failure(401, "请登录");
            servletResponse.setContentType("application/json;charset=utf-8");
            servletResponse.getWriter().write(JSON.toJSONString(res));
        }
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

    private boolean doExclude(HttpServletRequest request, FilterChain chain) {
        String servletPath = request.getServletPath();
        for (Predicate<String> s : exclude) {
            if (s.test(servletPath)) {
                log.info("排除这个请求：" + servletPath);
                return true;
            }
        }
        return false;
    }
}
