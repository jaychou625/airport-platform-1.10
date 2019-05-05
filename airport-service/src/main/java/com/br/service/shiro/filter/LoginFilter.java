package com.br.service.shiro.filter;

import com.alibaba.fastjson.JSON;
import com.br.entity.utils.Result;
import com.br.service.enumeration.ShiroEnumeration;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


/**
 * 登录过滤器
 *
 * @Author zyx
 * @Date 2019 02 21
 */
public class LoginFilter extends FormAuthenticationFilter {

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if (!isLoginRequest(request, response) && isAjaxRequest(request)) {
            response.setHeader("session-status", "timeout");
        }
        return super.onAccessDenied(request, response);
    }

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setCharacterEncoding("UTF-8");
        Result result = new Result();
        result.setStatus(ShiroEnumeration.LOGIN_SUCCESS.getStatus());
        result.setCode(ShiroEnumeration.LOGIN_SUCCESS.getCode());
        request.setAttribute("result", result);
        /*-------------------------------Ajax 请求登录成功 ------------------------------------*/
        if (isAjaxRequest(request)) {
            PrintWriter out = response.getWriter();
            out.println(JSON.toJSONString(result));
            out.flush();
            out.close();
        } else {
            this.issueSuccessRedirect(servletRequest, servletResponse);
        }
        return true;
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest servletRequest, ServletResponse servletResponse) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setCharacterEncoding("UTF-8");
        Result result = new Result();
        Class exceptionClass = e.getClass();
        if (exceptionClass == IncorrectCredentialsException.class) {
            result.setStatus(ShiroEnumeration.USERNAME_OR_PWD_ERROR.getStatus());
            result.setCode(ShiroEnumeration.USERNAME_OR_PWD_ERROR.getCode());
            result.getData().put("error", ShiroEnumeration.USERNAME_OR_PWD_ERROR.getMessage());
            request.setAttribute("result", result);
        }
        if (exceptionClass == UnknownAccountException.class) {
            result.setStatus(ShiroEnumeration.UNKNOWN_USER.getStatus());
            result.setCode(ShiroEnumeration.UNKNOWN_USER.getCode());
            result.getData().put("error", ShiroEnumeration.UNKNOWN_USER.getMessage());
            request.setAttribute("result", result);
        }
        try {
            /*-------------------------------Ajax 请求登录失败 ------------------------------------*/
            if (isAjaxRequest(request)) {
                if (isLoginRequest(request, response)) {
                    if (isLoginSubmission(request, response)) {
                        PrintWriter out = response.getWriter();
                        out.println(JSON.toJSONString(result));
                        out.flush();
                        out.close();
                        return true;
                    }
                }
            } else {
                this.setFailureAttribute(request, e);
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }


    /**
     * 判断是否是Ajax请求
     *
     * @param request HTTP请求
     * @return boolean
     */
    public boolean isAjaxRequest(HttpServletRequest request) {
        if ("XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"))) {
            return true;
        }
        return false;
    }
}
