package com.august.common.config;

import com.august.common.CacheSessionDAO;
import com.august.modules.system.shiro.FormAuthenticationFilter;
import com.august.common.SessionManager;
import com.august.modules.system.shiro.SystemAuthorizingRealm;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.cas.CasFilter;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.common.config
 * Author: August
 * Update: August(2015/10/9)
 * Description:权限管理模块相关配置信息
 */
@Configuration
@Description(value = "shiro权限管理模块相关配置信息")
public class ShiroConfig {
    /**
     * http://www.oschina.net/question/129540_89805
     * http://blog.csdn.net/yangnianbing110/article/details/19708907
     */
    //定义日志记录器
    private static final Logger LOGGER = LoggerFactory.getLogger(ShiroConfig.class);
    //应用路径
    @Value("${adminPath}")
    private String adminPath;

    //session超时时间
    @Value("${session.sessionTimeout}")
    private Long sessionTimeout;

    //定时清理失效会话
    @Value("${session.sessionTimeoutClean}")
    private Long sessionTimeoutClean;

    //引入系统授权认证操作域实例
    @Autowired
    private SystemAuthorizingRealm systemAuthorizingRealm;

    //引入表单授权认证操作过滤器实例
    @Autowired
    private FormAuthenticationFilter formAuthenticationFilter;

    /**
     * Shiro权限过滤过滤器定义
     * //     /static/** = anon
     * //     /userfiles/** = anon
     * //     ${adminPath}/cas = cas
     * //     ${adminPath}/login = authc
     * //     ${adminPath}/logout = logout
     * //     ${adminPath}/** = user
     * //     /act/rest/service/editor/** = perms[act:model:edit]
     * //     /act/rest/service/model/** = perms[act:model:edit]
     * //     /act/rest/service/** = user
     * //     /ReportServer/** = user
     *
     * @return
     */
    @Bean(name = "shiroFilterChainDefinitions")
    @Description("Shiro权限过滤过滤器定义")
    public Map<String, String> shiroFilterChainDefinitions() {
        Map<String, String> shiroFilterChainDefinitionsMap = new HashMap<String, String>();
        shiroFilterChainDefinitionsMap.put("/static/**", "anon");
        shiroFilterChainDefinitionsMap.put("/userFiles/**", "anon");
        shiroFilterChainDefinitionsMap.put(adminPath + "/cas", "cas");
        shiroFilterChainDefinitionsMap.put(adminPath + "/login", "authc");
        shiroFilterChainDefinitionsMap.put(adminPath + "/logout", "logout");
        shiroFilterChainDefinitionsMap.put(adminPath + "/**", "user");
        shiroFilterChainDefinitionsMap.put("/act/rest/service/editor/**", "perms[act:model:edit]");
        shiroFilterChainDefinitionsMap.put("/act/rest/service/model/**", "perms[act:model:edit]");
        shiroFilterChainDefinitionsMap.put("/act/rest/service/**", "user");
        shiroFilterChainDefinitionsMap.put("/ReportServer/**", "user");
        return shiroFilterChainDefinitionsMap;
    }

    /**
     * 声明shiro安全认证过滤器
     *
     * @return
     */
    @Bean(name = "shiroFilter")
    @Description(value = "声明shiro安全认证过滤器")
    public ShiroFilterFactoryBean shiroFilterFactoryBean() {
        //定义shiro过滤器工厂实例
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //设定权限管理配置
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager());
        //设定登录URL
        shiroFilterFactoryBean.setLoginUrl(adminPath + "/login");
        //设定登录成功的URL
        shiroFilterFactoryBean.setSuccessUrl(adminPath + "?welcome");
        //配置未授权URL路径信息
        shiroFilterFactoryBean.setUnauthorizedUrl(adminPath + "/403");
        //定义过滤器集合
        Map<String, Filter> filterMap = new LinkedHashMap<String, Filter>();
        //设定单点登录过滤器
        filterMap.put("cas", casFilter());
        //设定授权过滤器
        filterMap.put("authc", formAuthenticationFilter);
        //配置过滤器
        shiroFilterFactoryBean.setFilters(filterMap);
        //配置过滤路径
        shiroFilterFactoryBean.setFilterChainDefinitionMap(shiroFilterChainDefinitions());
        return shiroFilterFactoryBean;
    }

    /**
     * 定义CAS单点登录认证过滤器
     *
     * @return 实例化的单点登录认证过滤器
     */
    @Bean(name = "casFilter")
    @Description(value = "定义CAS单点登录认证过滤器")
    public CasFilter casFilter() {
        //定义单点登录认证过滤器
        CasFilter casFilter = new CasFilter();
        //设置登录失败转向的URL
        casFilter.setFailureUrl(adminPath + "/login");
        return casFilter;
    }

    /**
     * 创建默认的web权限管理控制器
     *
     * @return 实例化的权限控制器
     */
    @Bean(name = "securityManager")
    @Description("创建默认的web权限管理控制器")
    public DefaultWebSecurityManager defaultWebSecurityManager() {
        //声明默认web安全管理器
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        //设定自定义shiroRealm
        defaultWebSecurityManager.setRealm(systemAuthorizingRealm);
        //设定自定义session管理
        defaultWebSecurityManager.setSessionManager(sessionManager());
        //设定自定义缓存管理器
        defaultWebSecurityManager.setCacheManager(ehCacheManager());
        return defaultWebSecurityManager;
    }

    @Bean(name = "sessionManager")
    @Description("自定义会话管理配置")
    public SessionManager sessionManager() {
        //创建一个session管理器
        SessionManager sessionManager = new SessionManager();
        //定义sessionDAO数据库操作层
        sessionManager.setSessionDAO(sessionDao());
        //会话超时时间，单位：毫秒
        sessionManager.setGlobalSessionTimeout(sessionTimeout);
        //定时清理失效会话, 清理用户直接关闭浏览器造成的孤立会话
        sessionManager.setSessionValidationInterval(sessionTimeoutClean);
        //设定启用会话调度验证
        sessionManager.setSessionValidationSchedulerEnabled(Boolean.TRUE);
        //设定本系统sessionID会话ID
        sessionManager.setSessionIdCookie(simpleCookie());
        //设定本系统允许使用自定义sessionID
        sessionManager.setSessionIdCookieEnabled(Boolean.TRUE);
        return sessionManager;
    }

    @Bean(name = "sessionDAO")
    @Description("自定义Session存储容器")
    public CacheSessionDAO cacheSessionDAO() {
        //定义自定义会话信息存储容器
        CacheSessionDAO cacheSessionDAO = new CacheSessionDAO();
        //设置会话ID生成策略
        cacheSessionDAO.setSessionIdGenerator(idGen());
        //设定活动会话缓存名称
        cacheSessionDAO.setActiveSessionsCacheName("activeSessionsCache");
        //设定缓存管理器
        cacheSessionDAO.setCacheManager(ehCacheManager());
        return cacheSessionDAO;
    }

    /**
     * 指定本系统sessionID, 默认为: JSESSIONID
     * 问题: 与SERVLET容器名冲突, 如JETTY, TOMCAT 等默认JSESSIONID,
     * 当跳出SHIRO SERVLET时如ERROR-PAGE容器会为JSESSIONID重新分配值导致登录会话丢失!
     *
     * @return
     */
    @Bean(name = "sessionIdCookie")
    @Description("指定本系统sessionID")
    public SimpleCookie simpleCookie() {
        return new SimpleCookie("august.session.id");
    }


    @Bean(name = "shiroCacheManager")
    @Description("定义授权缓存管理器")
    public EhCacheManager ehCacheManager() {
        //声明授权缓存管理器
        EhCacheManager ehCacheManager = new EhCacheManager();
        //设置缓存管理实例
        ehCacheManager.setCacheManager(cacheManager());
        return ehCacheManager;
    }


    @Bean(name = "lifecycleBeanPostProcessor")
    @Description("保证实现了Shiro内部lifecycle函数的bean执行")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    @Description("AOP式方法级权限检查1")
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(Boolean.TRUE);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    @Description("AOP式方法级权限检查")
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(defaultWebSecurityManager());
        return authorizationAttributeSourceAdvisor;
    }
}
