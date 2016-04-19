package com.august.common;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Description;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Properties;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.common
 * Author: August
 * Update: August(2016/4/11)
 * Description:Properties文件载入工具类.
 * 可载入多个properties文件,
 * 相同的属性在最后载入的文件中的值将会覆盖之前的值，但以System的Property优先.
 */
@Description("定义properties文件载入工具类")
public class PropertiesLoader {
    //定义日志记录器
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesLoader.class);

    //实例化资源文件加载器
    private static ResourceLoader resourceLoader = new DefaultResourceLoader();

    //定义资源文件
    private final Properties properties;

    //初始化资源文件
    public PropertiesLoader(String... resourcePaths) {
        properties = loadProperties(resourcePaths);
    }

    public Properties getProperties() {
        return properties;
    }

    /**
     * 根据传入的key获取对应的值
     * 取出Property，但以System的Property优先,取不到返回空字符串.
     *
     * @param key 参数主键
     * @return 返回 对应的参数值
     */
    private String getValue(String key) {
        String systemProperty = System.getProperty(key);
        if (systemProperty != null) return systemProperty;
        if (properties.contains(key)) return properties.getProperty(key);
        return "";
    }

    /**
     * 取出String类型的Property，但以System的Property优先,如果都为Null则抛出异常.
     *
     * @param key 参数主键
     * @return 返回对应的配置信息
     */
    public String getProperty(String key) {
        String value = getValue(key);
        if (value == null) throw new NoSuchElementException();
        return value;
    }

    /**
     * 载入多个文件，文件路径用spring resource格式
     *
     * @param resourcePaths 文件路径
     * @return 配置信息
     */
    private Properties loadProperties(String... resourcePaths) {
        Properties properties = new Properties();
        for (String location : resourcePaths) {
            InputStream inputStream = null;
            try {
                Resource resource = resourceLoader.getResource(location);
                inputStream = resource.getInputStream();
                properties.load(inputStream);
            } catch (IOException e) {
                LOGGER.info("无法从指定的路径:" + location + "下找到对应的文件, " + e.getMessage());
            } finally {
                IOUtils.closeQuietly(inputStream);
            }
        }
        return properties;
    }

    /**
     * 根据给定的参数信息获取对应的参数值，如果没有则直接返回默认值
     * 取出String类型的Property，但以System的Property优先.如果都为Null则返回Default值.
     *
     * @param key          参数主键
     * @param defaultValue 默认值
     * @return 返回 获取到的参数值
     */
    public String getProperty(String key, String defaultValue) {
        String value = getValue(key);
        return value != null ? value : defaultValue;
    }

    /**
     * 取出Integer类型的Property，但以System的Property优先.如果都为Null或内容错误则抛出异常.
     */
    public Integer getInteger(String key) {
        String value = getValue(key);
        if (value == null) {
            throw new NoSuchElementException();
        }
        return Integer.valueOf(value);
    }

    /**
     * 取出Integer类型的Property，但以System的Property优先.如果都为Null则返回Default值，如果内容错误则抛出异常
     */
    public Integer getInteger(String key, Integer defaultValue) {
        String value = getValue(key);
        return value != null ? Integer.valueOf(value) : defaultValue;
    }

    /**
     * 取出Double类型的Property，但以System的Property优先.如果都为Null或内容错误则抛出异常.
     */
    public Double getDouble(String key) {
        String value = getValue(key);
        if (value == null) {
            throw new NoSuchElementException();
        }
        return Double.valueOf(value);
    }

    /**
     * 取出Double类型的Property，但以System的Property优先.如果都为Null则返回Default值，如果内容错误则抛出异常
     */
    public Double getDouble(String key, Integer defaultValue) {
        String value = getValue(key);
        return value != null ? Double.valueOf(value) : defaultValue;
    }

    /**
     * 取出Boolean类型的Property，但以System的Property优先.如果都为Null抛出异常,如果内容不是true/false则返回false.
     */
    public Boolean getBoolean(String key) {
        String value = getValue(key);
        if (value == null) {
            throw new NoSuchElementException();
        }
        return Boolean.valueOf(value);
    }

    /**
     * 取出Boolean类型的Property，但以System的Property优先.如果都为Null则返回Default值,如果内容不为true/false则返回false.
     */
    public Boolean getBoolean(String key, boolean defaultValue) {
        String value = getValue(key);
        return value != null ? Boolean.valueOf(value) : defaultValue;
    }

}
























