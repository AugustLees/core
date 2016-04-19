package com.august.common.config;

import com.august.modules.system.utils.StaticConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.common.config
 * Author: Administrator
 * Update: Administrator(2015/10/16)
 * Description: 启用缓存相关配置信息
 */
@Configuration
@EnableCaching//启用缓存注解
public class CachingConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(CachingConfig.class);


    /**
     * 实例化缓存管理工厂
     *
     * @return 缓存管理工厂Bean实例
     */
    @Bean
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
        LOGGER.debug("CachingConfig 中 cacheManager工厂类，指定ehcache.xml的位置……");
        EhCacheManagerFactoryBean ehCacheManagerFactoryBean = new EhCacheManagerFactoryBean();
        ehCacheManagerFactoryBean.setConfigLocation(new ClassPathResource(StaticConstant.CACHING_CONFIG_CLASSPATH));
        return ehCacheManagerFactoryBean;
    }

    /**
     * 设置缓存管理器
     *
     * @return 缓存管理器实例
     */
    @Bean
    public CacheManager cacheManager() {
        LOGGER.info("CachingConfig 中 声明cacheManager开启EhCacheCacheManager缓存管理器实例……");
        EhCacheCacheManager cacheManager = new EhCacheCacheManager();
        cacheManager.setCacheManager(ehCacheManagerFactoryBean().getObject());
        return cacheManager;
    }


    /**
     * 等价于xml文件中如下配置信息：
     *  <!-- 启用缓存注解 -->
     *  <cache:annotation-driven cache-manager="cacheManager" />
     *
     *
     *  <!-- cacheManager工厂类，指定ehcache.xml的位置 -->
     *  <bean id="cacheManagerFactory"
     *      class="org.springframework.cache.ehcache.EhCacheManagerFactoryBean"
     *      p:configLocation="classpath:/config/ehcache.xml" />
     *
     *  <!-- 声明cacheManager -->
     *  <bean id="cacheManager" class="org.springframework.cache.ehcache.EhCacheCacheManager"
     *      p:cacheManager-ref="cacheManagerFactory" />
     */
}
