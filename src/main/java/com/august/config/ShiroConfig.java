package com.august.config;

import com.august.utils.StaticConstant;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.config
 * Author: August
 * Update: August(2015/10/9)
 * Description:权限管理模块相关配置信息
 */
@Configuration
public class ShiroConfig {
    /**
     * http://www.oschina.net/question/129540_89805
     * http://blog.csdn.net/yangnianbing110/article/details/19708907
     */
    //定义日志记录器
    private static final Logger LOGGER = LoggerFactory.getLogger(ShiroConfig.class);

    @Bean(name = StaticConstant.WEB_INITIALIZER_SHIRO_FILTER)
    public ShiroFilterFactoryBean shiroFilterFactoryBean() {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager());
        shiroFilterFactoryBean.setLoginUrl("/login.do");
        shiroFilterFactoryBean.setSuccessUrl("/welcome.do");
        shiroFilterFactoryBean.setUnauthorizedUrl("/403.do");
        //配置过滤器
        shiroFilterFactoryBean.setFilters(null);
        //配置过滤路径
        shiroFilterFactoryBean.setFilterChainDefinitionMap(null);
        return shiroFilterFactoryBean;
    }

    /**
     * 创建默认的web权限管理控制器
     *
     * @return 实例化的权限控制器
     */
    @Bean(name = "securityManager")
    public DefaultWebSecurityManager defaultWebSecurityManager() {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager(;);
        defaultWebSecurityManager.setRealm(myShiroRealm());
        return defaultWebSecurityManager;
    }

    @Bean
    public MyShiroRealm myShiroRealm(){
        MyShiroRealm myShiroRealm=new MyShiroRealm();
        myShiroRealm.setbus
    }

}
