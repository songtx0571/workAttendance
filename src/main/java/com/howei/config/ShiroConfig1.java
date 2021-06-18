package com.howei.config;

import com.howei.config.redis.ShiroFormAuthenticationFilter;
import com.howei.realm.LoginRealm;
import org.apache.shiro.session.mgt.eis.MemorySessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.AnonymousFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig1 {

    @Value("${shiro.loginUrl}")
    private String masterLoginUrl;

    @Value("${shiro.jessionid}")
    private String jessionId;
    //取redis连接配置
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.password}")
    private String password;
    @Value("${spring.redis.timeout}")
    private int timeout;
    @Value("${spring.redis.isRedisCache}")
    private int isRedisCache;
    @Value("${shiro.conf.sessionTimeout}")
    private Integer maxAge;

    @Bean("loginRealm")
    @DependsOn(value = "lifecycleBeanPostProcessor")
    LoginRealm loginRealm() {
        LoginRealm loginRealm = new LoginRealm();
        return loginRealm;
    }

    /**
     * 配置shiro redisManager
     *
     * @return
     */
    @Bean("redisManager")
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(host);
        redisManager.setPort(port);
        redisManager.setPassword(password);
        redisManager.setExpire(28800);// 配置过期时间：8小时
        return redisManager;
    }

    @Bean("sessionDAO")
    SessionDAO sessionDAO(@Qualifier("redisManager")RedisManager redisManager) {
        if (1 == isRedisCache) {
            RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
            redisSessionDAO.setRedisManager(redisManager);
            return redisSessionDAO;
        } else {
            MemorySessionDAO sessionDAO = new MemorySessionDAO();
            return sessionDAO;
        }
    }

    /**
     * 给shiro的sessionId默认的JSSESSIONID名字改掉
     *
     * @return
     */
    @Bean(name = "sessionIdCookie")
    public SimpleCookie getSessionIdCookie() {
        SimpleCookie simpleCookie = new SimpleCookie(jessionId);
        simpleCookie.setPath("/");
        simpleCookie.setHttpOnly(true);
        simpleCookie.setMaxAge(maxAge);
        return simpleCookie;
    }

    @Bean(name = "sessionManager")
    public DefaultWebSessionManager sessionManager(@Qualifier("sessionDAO")SessionDAO sessionDAO,@Qualifier("sessionIdCookie") SimpleCookie simpleCookie) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setDeleteInvalidSessions(true);
        sessionManager.setSessionDAO(sessionDAO);
        //去掉shiro登录时url里的JSESSIONID
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionValidationInterval(28800000);
        //是否开启删除无效的session对象  默认为true
        sessionManager.setDeleteInvalidSessions(true);
        sessionManager.setSessionIdCookie(simpleCookie);
        return sessionManager;
    }

    /**
     * cacheManager 缓存 redis实现
     *
     * @return
     */
    @Bean("redisCacheManager")
    public RedisCacheManager redisCacheManager(@Qualifier("redisManager") RedisManager redisManager) {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager);
        return redisCacheManager;
    }

    @Bean(name = "securityManager")
    public DefaultWebSecurityManager securityManager(@Qualifier("loginRealm") LoginRealm loginRealm, @Qualifier("sessionManager") DefaultWebSessionManager sessionManager, @Qualifier("redisCacheManager") RedisCacheManager redisCacheManager) {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(loginRealm);
        manager.setCacheManager(redisCacheManager);
        manager.setSessionManager(sessionManager);
        return manager;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        filterRegistration.setFilter(new DelegatingFilterProxy("shiroFilter"));
        filterRegistration.setEnabled(true);
        filterRegistration.addUrlPatterns("/*");
        filterRegistration.setDispatcherTypes(DispatcherType.REQUEST);
        return filterRegistration;
    }

    @Bean(name = "shiroFilter")
    protected ShiroFilterFactoryBean shiroFilter(@Qualifier("securityManager") DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean bean = new MShiroFilterFactoryBean(); //指向自定义过滤器，自定义过滤器对js/css等忽略
        bean.setSecurityManager(securityManager);
        bean.setLoginUrl(masterLoginUrl);
        Map<String, Filter> filters = new LinkedHashMap<>();
        filters.put("anon", new AnonymousFilter());
        bean.setFilters(filters);
        Map<String,String> map1=new HashMap<>();
        map1.put("/logout","logout");
        //map1.put("/**","user");
        bean.setSuccessUrl("/home");
        bean.setFilterChainDefinitionMap(map1);
        //自定义过滤类
        ShiroFormAuthenticationFilter shiroFormAuthenticationFilter = new ShiroFormAuthenticationFilter();
        Map<String,Filter> filterMap = new HashMap<>();
        filterMap.put("user",shiroFormAuthenticationFilter);
        bean.setFilters(filterMap);
        return bean;
    }

    /**
     *  开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
     * 配置以下两个bean(DefaultAdvisorAutoProxyCreator和AuthorizationAttributeSourceAdvisor)即可实现此功能
     * @return
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    /**
     * 开启aop注解支持
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(org.apache.shiro.mgt.SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * 该类如果不设置为static，@Value注解就无效，原因未知
     *
     * @return
     */
    @Bean
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

}
