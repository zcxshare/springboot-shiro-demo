package com.zcx.demo.shiroweb.core.config;

import com.sun.tracing.ProbeName;
import com.zcx.demo.shiroweb.shiro.filter.RoleRoFilter;
import com.zcx.demo.shiroweb.shiro.manager.CustomSessionManager;
import com.zcx.demo.shiroweb.shiro.realm.CustomRealm;
import com.zcx.demo.shiroweb.shiro.session.RedisSessionDAO;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Bean
    public CredentialsMatcher credentialsMatcher(){
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher("md5");
        matcher.setHashIterations(1);
        return matcher;
    }

    @Bean
    public CustomRealm customRealm(CredentialsMatcher credentialsMatcher){
        CustomRealm customRealm = new CustomRealm();
        customRealm.setCredentialsMatcher(credentialsMatcher);
        return customRealm;
    }

    @Bean
    public SecurityManager securityManager(CustomRealm customRealm, SessionManager sessionManager, CacheManager cacheManager
            ,CookieRememberMeManager rememberMeManager){
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setSessionManager(sessionManager);
        defaultWebSecurityManager.setCacheManager(cacheManager);
        defaultWebSecurityManager.setRememberMeManager(rememberMeManager);
        defaultWebSecurityManager.setRealm(customRealm);
        return defaultWebSecurityManager;
    }

    @Bean
    public CookieRememberMeManager cookieRememberMeManager() {
        CookieRememberMeManager rememberMeManager = new CookieRememberMeManager();
        Cookie cookie = new SimpleCookie("rememberMe");
        cookie.setMaxAge(1000000);
        rememberMeManager.setCookie(cookie);
        return rememberMeManager;
    }

    @Bean
    public SessionManager sessionManager(RedisSessionDAO sessionDao){
        DefaultWebSessionManager defaultWebSessionManager = new CustomSessionManager();
        defaultWebSessionManager.setSessionDAO(sessionDao);
        return defaultWebSessionManager;
    }
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setLoginUrl("/login.html");
        shiroFilterFactoryBean.setUnauthorizedUrl("/500.html");
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        HashMap<String, String> map = new HashMap<>();
        map.put("/user/login","anon");
        map.put("/user/testRole1","roles[admin]");
        map.put("/user/testRole2","roleOr[admin,admin1]");
        map.put("/user/testPerm1","perms[user:update]");
        map.put("/user/testPerm2","perms[user:update,user:deletee]");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("roleOr",new RoleRoFilter());
        shiroFilterFactoryBean.setFilters(filterMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

}