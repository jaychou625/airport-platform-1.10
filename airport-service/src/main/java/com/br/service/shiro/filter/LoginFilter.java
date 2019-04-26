package com.br.service.shiro.filter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 登录过滤器
 *
 * @Author zyx
 * @Date 2019 02 21
 */
public class LoginFilter extends FormAuthenticationFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object mappedValue) {
        Subject subject = this.getSubject(servletRequest, servletResponse);
        if (isLoginRequest(servletRequest, servletResponse)) {
            if (isLoginSubmission(servletRequest, servletResponse)) {
                String username = this.getUsername(servletRequest);
                if (username != null) {
                    subject.logout();
                }
            }
        }
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if (!subject.isAuthenticated())
            if (request.getHeader("x-requested-with") != null
                    && request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {
                response.setHeader("session-status", "timeout");
                return false;
            }
        return super.isAccessAllowed(request, response, mappedValue);
    }

}
