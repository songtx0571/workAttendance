package com.howei.config.redis;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

@Component
//@EnableAutoConfiguration(exclude = ErrorMvcAutoConfiguration.class)
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            //Verify whether the user has logged in. If so, kick out the previous user and update the user information in the cache
            Subject subject = SecurityUtils.getSubject();
            Serializable token = subject.getSession().getId();
            MyRedisManager redisManager = MyRedisManager.getRedisSingleton();
            redisManager.setHost("localhost");
            redisManager.setPort(6379);
            redisManager.setTimeout(1800);
            redisManager.setPassword("12345");
            redisManager.init();
            //Get user ID
            String userId = redisManager.get("user_token_"+token.toString());
            if(StringUtils.isNotBlank(userId)) {
                String tokenPre = redisManager.get("user_id_"+userId);
                if(!token.equals(tokenPre)) {
                    //Redirect to login.html
                    redirect(request, response);
                    return false;
                }else {
                    redisManager.expire("user_token_"+token.toString(), 60*30);
                }
            }else {
                redirect(request, response);
                return false;
            }
        } catch (Exception e) {
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }
    //对于请求是ajax请求重定向问题的处理方法
    public void redirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //获取当前请求的路径
        String basePath = request.getScheme() + "://" + request.getServerName() + ":"  + request.getServerPort()+request.getContextPath();
        //response.getOutputStream().write("账号在别处登录。".getBytes("UTF-8"));
        //如果request.getHeader("X-Requested-With") 返回的是"XMLHttpRequest"说明就是ajax请求，需要特殊处理 否则直接重定向就可以了
        if("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))){
            //告诉ajax我是重定向
            response.setHeader("REDIRECT", "REDIRECT");
            //告诉ajax我重定向的路径
            response.setHeader("CONTENTPATH", basePath+"/login");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }else{
            response.sendRedirect(basePath + "/login");
        }
    }

}
