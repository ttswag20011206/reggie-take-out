package com.zkx.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.zkx.reggie.commons.BaseContext;
import com.zkx.reggie.commons.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String requestUrl = request.getRequestURI();

        log.info(requestUrl);

        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",

        };

        //判断本次需不需要处理
        boolean check = check(urls, requestUrl);

        //如果不需要处理，则直接放行
        if(check){
            filterChain.doFilter(request, response);
            return;
        }
        //判断登录状态，如果已经登录就放行
        if(request.getSession().getAttribute("employee") != null){
            Long userId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(userId);
            filterChain.doFilter(request, response);
            return;
        }
        // 如果没有登录就返回登录页面，
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));



    }

    public boolean check(String [] urls,String requestUrl) {
        for (String url : urls){
            boolean match = PATH_MATCHER.match(url, requestUrl);
            if(match){
                return true;
            }
        }
        return false;
    }
}

