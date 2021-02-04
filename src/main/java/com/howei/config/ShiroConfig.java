package com.howei.config;

import com.howei.realm.LoginRealm;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.AnonymousFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
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
public class ShiroConfig {

    @Value("${shiro.loginUrl}")
    private String masterLoginUrl;

    @Value("${shiro.jessionid}")
    private String jessionId;

    //将验证方式加入容器
    @Bean
    @DependsOn(value="lifecycleBeanPostProcessor")
    public LoginRealm loginRealm(){
        LoginRealm userRealm = new LoginRealm();
        userRealm.setCredentialsMatcher(credentialsMatcher());
        return userRealm;
    }

    /**
     * FilterRegistrationBean
     * @return
     */
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        filterRegistration.setFilter(new DelegatingFilterProxy("shiroFilter"));
        filterRegistration.setEnabled(true);
        filterRegistration.addUrlPatterns("/*");
        filterRegistration.setDispatcherTypes(DispatcherType.REQUEST);
        return filterRegistration;
    }

    /**
     * @see ShiroFilterFactoryBean
     * @return
     */
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(@Qualifier("securityManager") DefaultWebSecurityManager securityManager){
        ShiroFilterFactoryBean bean = new MShiroFilterFactoryBean(); //指向自定义过滤器，自定义过滤器对js/css等忽略
        bean.setSecurityManager(securityManager);
        bean.setLoginUrl(masterLoginUrl);
        Map<String, Filter>filters = new LinkedHashMap<>();
        filters.put("anon", new AnonymousFilter());
        //配置自定义登出 覆盖 logout 之前默认的LogoutFilter
        filters.put("logout", shiroLogoutFilter());
        bean.setFilters(filters);
        //首页
        bean.setSuccessUrl("/home");
        return bean;
    }


    @Bean
    public RedisSessionDao getRedisSessionDao(){
        return new RedisSessionDao();
    }
    /**
     * @see DefaultWebSessionManager
     * @return
     */
    @Bean(name="sessionManager")
    public DefaultWebSessionManager defaultWebSessionManager(RedisSessionDao redisSessionDao) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        Cookie cookie=sessionManager.getSessionIdCookie();
        cookie.setName("sessionId");
        sessionManager.setGlobalSessionTimeout(5000);
        //sessionManager.setGlobalSessionTimeout(14400000); //4小时
        sessionManager.setSessionDAO(redisSessionDao);
        //是否开启定时调度器进行检测过期session 默认为true
        sessionManager.setSessionValidationSchedulerEnabled(true);
        //设置session失效的扫描时间, 清理用户直接关闭浏览器造成的孤立会话 默认为 1个小时
        //设置该属性 就不需要设置 ExecutorServiceSessionValidationScheduler 底层也是默认自动调用ExecutorServiceSessionValidationScheduler
        //设置为 1分钟 用来测试
        sessionManager.setSessionValidationInterval(5000);
        //sessionManager.setSessionValidationInterval(60000);
        //是否开启删除无效的session对象  默认为true
        sessionManager.setDeleteInvalidSessions(true);
        sessionManager.setSessionIdCookie(new SimpleCookie("sessionId"));
        return sessionManager;
    }

    /**
     * @see org.apache.shiro.mgt.SecurityManager
     * @return
     */
    @Bean(name="securityManager")
    public DefaultWebSecurityManager securityManager(LoginRealm userRealm, RedisSessionDao redisSessionDao) {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(userRealm);
        manager.setSessionManager(defaultWebSessionManager(redisSessionDao));
        return manager;
    }

    /**
     * 给shiro的sessionId默认的JSSESSIONID名字改掉
     * @return
     */
    @Bean(name="sessionIdCookie")
    public SimpleCookie getSessionIdCookie(){
        SimpleCookie simpleCookie = new SimpleCookie(jessionId);
        //maxAge=-1表示浏览器关闭时失效此Cookie
        simpleCookie.setMaxAge(-1);
        return simpleCookie;
    }

    @Bean(name="credentialsMatcher")
    public CredentialsMatcher credentialsMatcher() {
        return new RetryLimitHashedCredentialsMatcher();
    }

    /**
     * 该类如果不设置为static，@Value注解就无效，原因未知
     * @return
     */
    @Bean
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 配置LogoutFilter
     * @return
     */
    public ShiroLogoutFilter shiroLogoutFilter(){
        ShiroLogoutFilter shiroLogoutFilter = new ShiroLogoutFilter();
        //配置登出后重定向的地址，等出后配置跳转到登录接口
        shiroLogoutFilter.setRedirectUrl("/");
        return shiroLogoutFilter;
    }
}
