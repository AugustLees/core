package com.august.utils;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.utils
 * Author: August
 * Update: August(2015/9/11)
 * Description:定义该应用常用静态常量
 */
public class StaticConstant {
    //定义web应用初始化servlet的名称
    public static final String WEB_INITIALIZER_SERVLET_NAME = "dispatcher";

    //定义WEB应用显示名称
    public static final String WEB_INITIALIZER_DISPLAY_NAME = "测试应用";

    //定义WEB应用初始化字符集编码
    public static final String WEB_INITIALIZER_CHARACTER_ENCODING = "characterEncodingFilter";

    //定义web应用初始化servlet的映射路径
    public static final String WEB_INITIALIZER_SERVLET_MAPPING = "*.html";

    //定义WEB应用中shiro过滤器的名称
    public static final String WEB_INITIALIZER_SHIRO_FILTER = "shiroDelegatingFilterProxy";


    //定义定时任务出发时间表达式
    public static final String CRON_TRIGGER = "0 0/1 * * * ?";//每隔5分钟触发一次

    //定义该应用使用字符集编码
    public static final String CHARACTER_ENCODING = "utf-8";//字符编码格式

    //APPCONFIG配置文件基本扫描路径
    public static final String APP_CONFIG_BASE_PACKAGES = "com.sample.project.config";

    //数据库配置信息扫描路径
    public static final String DATABASE_CONFIG_PACKAGE_TO_SCAN = "com.sample.model";

    //数据源配置基本配置文件路径
    public static final String DATA_SOURCE_CONFIG_PROPERTY_SOURCE = "classpath:/config/properties/db.properties";

    //SPRING_MVC扫描基本路径
    public static final String SPRING_MVC_CONFIG_BASE_PACKAGES = "com.sample.project";

    //切入点配置基本路径
    public static final String POINTCUT_CONFIG_BASE_PACKAGES = "com.sample.project.aop";

    //定义切面表达式
    public static final String POINTCUT_ASPECT_POINT_CUT = "execution(* com.sample.project.service..*.*(..))";

    //定义切面执行方法
    public static final String POINTCUT_ADVICE_METHOD = "com.sample.project.aop.PointcutsDefinition.inServiceLayer()";

    //定义SPRING_MVC中页面解析前缀
    public static final String SPRING_MVC_CONFIG_PREFIX = "/WEB-INF/views/";

    //定义SPRING_MVC中页面解析后缀
    public static final String SPRING_MVC_CONFIG_SUFFIX = ".jsp";
}
