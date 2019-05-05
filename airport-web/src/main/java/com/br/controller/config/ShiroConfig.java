package com.br.controller.config;

import com.br.service.constant.RequestRouteConstant;
import com.br.service.shiro.APShiroRealm;
import com.br.service.shiro.filter.LoginFilter;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.AbstractSessionManager;
import org.apache.shiro.session.mgt.eis.MemorySessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shiro 配置
 *
 * @Author zyx
 * @Date 2019 02 21
 */
@Configuration
public class ShiroConfig {

    /**
     * Shiro 记住我Cookie
     *
     * @return SimpleCookie
     */
    @Bean
    public SimpleCookie rememberMeCookie() {
        SimpleCookie rememberMeCookie = new SimpleCookie();
        rememberMeCookie.setName("rememberMe");
        rememberMeCookie.setMaxAge(604800);
        rememberMeCookie.setHttpOnly(true);
        return rememberMeCookie;
    }

    /**
     * Shiro 记住我管理
     *
     * @return CookieRememberMeManager
     */
    @Bean
    public CookieRememberMeManager cookieRememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        cookieRememberMeManager.setCipherKey(Base64.decode("IonwzK5Iytvy029yX82vDw=="));
        return cookieRememberMeManager;
    }

    /**
     * Shiro 数据域
     *
     * @return APShiroRealm
     */
    @Bean
    public APShiroRealm apShiroRealm() {
        APShiroRealm apShiroRealm = new APShiroRealm();
        apShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return apShiroRealm;
    }

    /**
     * 密码转换器
     *
     * @return HashedCredentialsMatcher
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        hashedCredentialsMatcher.setHashIterations(1);
        return hashedCredentialsMatcher;
    }


    /**
     * 内存SessionDao
     *
     * @return
     */
    @Bean
    public SessionDAO memorySessionDAO() {
        return new MemorySessionDAO();
    }

    /**
     * WebSessionManager 实例
     *
     * @return DefaultWebSessionManager
     */
    @Bean
    public DefaultWebSessionManager webSessionManager() {
        DefaultWebSessionManager webSessionManager = new DefaultWebSessionManager();
        webSessionManager.setGlobalSessionTimeout(AbstractSessionManager.DEFAULT_GLOBAL_SESSION_TIMEOUT);
        webSessionManager.setSessionDAO(memorySessionDAO());
        webSessionManager.setSessionIdUrlRewritingEnabled(false);
        return webSessionManager;
    }

    /**
     * SecurityManager 实例
     *
     * @return SecurityManager
     */
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager webSecurityManager = new DefaultWebSecurityManager();
        webSecurityManager.setRealm(apShiroRealm());
        webSecurityManager.setSessionManager(webSessionManager());
        webSecurityManager.setRememberMeManager(cookieRememberMeManager());
        return webSecurityManager;
    }

    /**
     * ShiroFilterFactoryBean 实例
     *
     * @return ShiroFilterFactoryBean
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean() {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager());
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("authc", new LoginFilter());
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put(RequestRouteConstant.REQUEST_ROUTE_CSS, "anon");
        filterChainDefinitionMap.put(RequestRouteConstant.REQUEST_ROUTE_JS, "anon");
        filterChainDefinitionMap.put(RequestRouteConstant.REQUEST_ROUTE_IMAGES, "anon");
        filterChainDefinitionMap.put(RequestRouteConstant.REQUEST_ROUTE_WEBSOCKET, "anon");
        filterChainDefinitionMap.put(RequestRouteConstant.REQUEST_ROUTE_MAP_RESOURCES, "anon");
        filterChainDefinitionMap.put("/testTaskStatus", "anon");
        /* 测试终端 start */
        filterChainDefinitionMap.put(RequestRouteConstant.REQUEST_ROUTE_SYSOPS + RequestRouteConstant.REQUEST_ROUTE_SYSOPS_FIND_CAR, "anon");
        filterChainDefinitionMap.put(RequestRouteConstant.REQUEST_ROUTE_MONITOR + RequestRouteConstant.REQUEST_ROUTE_TRAFFIC_INFO, "anon");
        filterChainDefinitionMap.put(RequestRouteConstant.REQUEST_ROUTE_MONITOR + RequestRouteConstant.REQUEST_ROUTE_TRAFFIC_TASK, "anon");
        /* 测试终端 end */
        filterChainDefinitionMap.put(RequestRouteConstant.REQUEST_ROUTE_LOGOUT, "logout");
        filterChainDefinitionMap.put(RequestRouteConstant.REQUEST_ROUTE_UNAUTHED, "anon");
        filterChainDefinitionMap.put(RequestRouteConstant.REQUEST_ROUTE_MAIN, "user");
        filterChainDefinitionMap.put(RequestRouteConstant.REQUEST_ROUTE_HOME, "user");
        filterChainDefinitionMap.put("/*", "authc");
        shiroFilterFactoryBean.setFilters(filterMap);
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        shiroFilterFactoryBean.setLoginUrl(RequestRouteConstant.REQUEST_ROUTE_LOGIN);
        shiroFilterFactoryBean.setSuccessUrl(RequestRouteConstant.REQUEST_ROUTE_MAIN);
        shiroFilterFactoryBean.setUnauthorizedUrl(RequestRouteConstant.REQUEST_ROUTE_UNAUTHED);
        return shiroFilterFactoryBean;
    }

    /**
     * 开启 Shrio AOP 注解支持
     *
     * @param securityManager
     * @return AuthorizationAttributeSourceAdvisor
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * 开启Shiro 权限注解
     *
     * @return DefaultAdvisorAutoProxyCreator
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    /**
     * Shiro 生命周期
     *
     * @return LifecycleBeanPostProcessor
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

}
