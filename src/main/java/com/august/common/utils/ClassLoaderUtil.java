package com.august.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Description;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.modules.system.utils
 * Author: August
 * Update: August(2016/4/12)
 * Description:
 * 用来加载类，classpath下的资源文件，属性文件等。
 * getExtendResource(StringrelativePath)方法，可以使用../符号来加载classpath外部的资源。
 */
@Description("用来加载类，classpath下的资源文件，属性文件等")
public class ClassLoaderUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassLoaderUtil.class);

    /**
     * 加载Java类。 使用全限定类名
     *
     * @param className 全限定名
     * @return 返回对应的Java类
     */
    public static Class loadClass(String className) {
        try {
            //获取类加载器，完成指定类名的加载
            return getClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("class not found '" + className + "'", e);
        }
    }

    /**
     * 得到类加载器
     *
     * @return 程序中的类加载器
     */
    public static ClassLoader getClassLoader() {
        return ClassLoaderUtil.class.getClassLoader();
    }

    /**
     * 提供相对于classpath的资源路径，返回文件的输入流
     *
     * @param relativePath 必须传递资源的相对路径。是相对于classpath的路径。如果需要查找classpath外部的资源，需要使用../来查找
     * @return 文件输入流
     * @throws MalformedURLException
     * @throws IOException
     */
    public static InputStream getStream(String relativePath) throws MalformedURLException, IOException {
        //如果路径不包含../则创建类加载器并以流的方式获取指定路径的信息
        if (!relativePath.contains("../")) {
            return getClassLoader().getResourceAsStream(relativePath);
        } else {
            //如果是绝对路径，则需要直接加载指定路径下的文件流
            return ClassLoaderUtil.getStreamByExtendResource(relativePath);
        }

    }

    /**
     * 根据提供的URL路径，返回文件的输入流
     *
     * @param url URL地址
     * @return 返回文件输入流
     * @throws IOException 可能遇到的异常
     */
    public static InputStream getStream(URL url) throws IOException {
        //如果URL不是空则打开流模式
        if (url != null) {
            return url.openStream();
        } else {
            return null;
        }
    }

    /**
     * 根据指定的路径获取对应的资源文件流
     *
     * @param relativePath 必须传递资源的相对路径。是相对于classpath的路径。如果需要查找classpath外部的资源，需要使用../来查找
     * @return
     * @throws IOException
     */
    public static InputStream getStreamByExtendResource(String relativePath) throws IOException {
        return ClassLoaderUtil.getStream(ClassLoaderUtil.getExtendResource(relativePath));
    }

    /**
     * 提供相对于classpath的资源路径，返回属性对象，它是一个散列表
     *
     * @param resource 资源路径
     * @return
     */
    public static Properties getProperties(String resource) {
        //获取配置文件信息实例
        Properties properties = new Properties();
        try {
            //加载配置文件信息
            properties.load(getStream(resource));
        } catch (IOException e) {
            throw new RuntimeException("couldn't load properties file '" + resource + "'", e);
        }
        return properties;
    }

    /**
     * 得到本Class所在的ClassLoader的ClassPath的绝对路径。
     * URL形式的
     *
     * @return 本类的绝对路径
     */
    public static String getAbsolutePathOfClassLoaderClassPath() {
        LOGGER.info(ClassLoaderUtil.getClassLoader().getResource("").toString());
        return ClassLoaderUtil.getClassLoader().getResource("").toString();
    }

    /**
     * 根据传入资源路径获取资源的绝对URL
     *
     * @param relativePath 必须传递资源的相对路径。是相对于classpath的路径。如果需要查找classpath外部的资源，需要使用../来查找
     * @return 资源的绝对URL
     * @throws MalformedURLException
     */
    public static URL getExtendResource(String relativePath) throws MalformedURLException {

        LOGGER.info("传入的相对路径：" + relativePath);
        if (!relativePath.contains("../")) {
            return ClassLoaderUtil.getResource(relativePath);

        }
        String classPathAbsolutePath = ClassLoaderUtil.getAbsolutePathOfClassLoaderClassPath();
        if (relativePath.substring(0, 1).equals("/")) {
            relativePath = relativePath.substring(1);
        }
        LOGGER.info(String.valueOf(relativePath.lastIndexOf("../")));

        String wildcardString = relativePath.substring(0, relativePath.lastIndexOf("../") + 3);
        relativePath = relativePath.substring(relativePath.lastIndexOf("../") + 3);
        int containSum = ClassLoaderUtil.containSum(wildcardString, "../");
        classPathAbsolutePath = ClassLoaderUtil.cutLastString(classPathAbsolutePath, "/", containSum);
        String resourceAbsolutePath = classPathAbsolutePath + relativePath;
        LOGGER.info("绝对路径：" + resourceAbsolutePath);
        URL resourceAbsoluteURL = new URL(resourceAbsolutePath);
        return resourceAbsoluteURL;
    }

    /**
     * 某个字符串中的某些内容出现的次数
     *
     * @param source 目标资源
     * @param dest   目标内容
     * @return 对应次数
     */
    private static int containSum(String source, String dest) {
        int containSum = 0;
        //内容长度
        int destLength = dest.length();
        //遍历内容，如果目标资源中存在制定的内容，则计数加1且
        while (source.contains(dest)) {
            containSum = containSum + 1;
            source = source.substring(destLength);
        }
        return containSum;
    }

    /**
     * 将传入的内容截取到最后一个标识之前的指定数量的长度
     *
     * @param source 需要截取的内容
     * @param dest   截止的内容
     * @param num    需要截取的长度
     * @return
     */
    private static String cutLastString(String source, String dest, int num) {
        for (int i = 0; i < num; i++) {
            source = source.substring(0, source.lastIndexOf(dest, source.length() - 2) + 1);
        }
        return source;
    }

    /**
     * 根据传入的相对于classPath的路径，加载对应的URL地址
     *
     * @param resource 资源信息
     * @return
     */
    public static URL getResource(String resource) {
        LOGGER.info("传入的相对于classpath的路径：" + resource);
        return ClassLoaderUtil.getClassLoader().getResource(resource);
    }

}
