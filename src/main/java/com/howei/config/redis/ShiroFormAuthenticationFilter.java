package com.howei.config.redis;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;

public class ShiroFormAuthenticationFilter extends FormAuthenticationFilter {

    public static final String DEFAULT_PATH_SEPARATOR = "/";
    private String pathSeparator = DEFAULT_PATH_SEPARATOR;

    /**
     * 重写验证失效后
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if (isLoginRequest(request, response)) {
            if (isLoginSubmission(request, response)) {
                return executeLogin(request, response);
            } else {
                return true;
            }
        } else {
            String requestURI = getPathWithinApplication(request);
            System.out.println(requestURI);
            HttpServletRequest httpServletRequest = (HttpServletRequest)request;
            System.out.println(httpServletRequest.getContextPath());
            //是ajax请求重定向
            if(isAjax(request)){
                PrintWriter out = response.getWriter();
                out.println("8888");
            }else{
                //针对前段请求window.location.href
                if(requestURI!=null&&requestURI.contains("/doc")){
                    response.setContentType("textml;charset=gb2312");
                    PrintWriter out = response.getWriter();
                    out.println("<script language='javascript' type='text/javascript'>");
                    out.println("alert('由于你长时间没有操作,导致Session失效!请你重新登录!');top.location.href='"+httpServletRequest.getContextPath()+"/login'");
                    out.println("</script>");
                }else {
                    //针对Form表单提交
                    this.saveRequestAndRedirectToLogin(request, response);
                }
            }

            return false;
        }
    }
    private static boolean isAjax(ServletRequest request){
        String header = ((HttpServletRequest) request).getHeader("X-Requested-With");
        if("XMLHttpRequest".equalsIgnoreCase(header)){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
