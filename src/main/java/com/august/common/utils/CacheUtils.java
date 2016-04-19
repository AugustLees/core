package com.august.common.utils;

import com.august.common.SpringUtils;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.springframework.context.annotation.Description;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.common.utils
 * Author: August
 * Update: August(2016/4/14)
 * Description:Cache缓存工具类
 */
@Description("Cache缓存工具类")
public class CacheUtils {
    //从spring上下文中获取缓存管理器实例
    private static CacheManager cacheManager = SpringUtils.getBean("cacheManager");

    //定义系统缓存标识
    private static final String SYS_CACHE = "sysCache";

    /**
     * 根据key获取对应的缓存对象
     * 获取SYS_CACHE缓存
     *
     * @param key 需要获取的主键
     * @return 对应的缓存对象
     */
    public static Object get(String key) {
        return get(SYS_CACHE, key);
    }


    /**
     * 获取缓存信息
     *
     * @param cacheName 缓存标识
     * @param key       缓存主键
     * @return 返回缓存对象
     */
    public static Object get(String cacheName, String key) {
        //获取指定缓存标识下的主键为key的缓存信息节点，并返回对应的节点值
        Element element = getCache(cacheName).get(key);
        return element == null ? null : element.getObjectValue();
    }

    /**
     * 根据缓存标识获取缓存信息，如果没有则创建一个
     *
     * @param cacheName 缓存标识
     * @return 缓存信息对象
     */
    private static Cache getCache(String cacheName) {
        //根据缓存标识获取缓存对象
        Cache cache = cacheManager.getCache(cacheName);
        if (cache == null) {
            //利用缓存管理器，创建一个缓存对象
            cacheManager.addCache(cacheName);
            //获取该缓存对象
            cache = cacheManager.getCache(cacheName);
            //设定该缓存永久有效
            cache.getCacheConfiguration().setEternal(true);
        }
        return cache;
    }

    /**
     * 写入SYS_CACHE中缓存
     *
     * @param key   主键
     * @param value 值
     */
    public static void put(String key, Object value) {
        put(SYS_CACHE, key, value);
    }

    /**
     * 写入指定缓存名称节点下的缓存对象
     *
     * @param cacheName 缓存节点
     * @param key       主键
     * @param value     键值
     */
    public static void put(String cacheName, String key, Object value) {
        //创建一个缓存节点
        Element element = new Element(key, value);
        //将缓存对象放入指定节点中
        getCache(cacheName).put(element);
    }

    /**
     * 从SYS_CACHE中移除主键为key的缓存节点
     *
     * @param key 缓存主键
     */
    public static void remove(String key) {
        remove(SYS_CACHE, key);
    }

    /**
     * 从cacheName中移除主键为key的缓存节点
     *
     * @param cacheName 缓存节点名称
     * @param key       主键
     */
    public static void remove(String cacheName, String key) {
        getCache(cacheName).remove(key);
    }

    public static CacheManager getCacheManager() {
        return cacheManager;
    }
}
