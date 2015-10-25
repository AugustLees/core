package com.august.utils;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.utils
 * Author: August
 * Update: August(2015/9/11)
 * Description:定义该应用常用静态常量
 */
public class StaticConstant {
    //定义WEB应用显示名称
    public static final String WEB_INITIALIZER_DISPLAY_NAME = "测试应用";

    //定义WEB应用下Log4j配置名称
    public static final String WEB_INITIALIZER_LOG4J_CONFIG_LOCATION = "log4jConfigLocation";

    //定义WEB应用下Log4j配置路径
    public static final String WEB_INITIALIZER_LOG4J_CONFIG_LOCATION_PATH = "classpath:config/log4j.properties";

    //定义WEB应用下spring默认刷新Log4j配置文件间隔
    public static final String WEB_INITIALIZER_LOG4J_REFRESH_INTERVAL = "log4jRefreshInterval";

    //定义WEB应用下spring默认刷新Log4j配置文件时间长度,单位为millisecond
    public static final String WEB_INITIALIZER_LOG4J_REFRESH_INTERVAL_TIME = "60000";

    //定义WEB应用下使用字符集编码
    public static final String CHARACTER = "UTF-8";//字符编码格式

    //定义WEB应用下初始化字符集名称
    public static final String WEB_INITIALIZER_CHARACTER_ENCODING_FILTER = "characterEncodingFilter";

    //定义WEB应用下字符集编码映射的URL
    public static final String WEB_INITIALIZER_CHARACTER_ENCODING_URL = "/*";

    //定义web应用下启用druid监控信息配置路径
    public static final String WEB_INITIALIZER_IS_EXCLUSION = "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*";

    //定义web应用下druid过滤器名称
    public static final String WEB_INITIALIZER_DRUID_WEB_STAT_FILTER = "druidWebStatFilter";

    //定义web应用下druid过滤映射路径
    public static final String WEB_INITIALIZER_DRUID_WEB_STAT_FILTER_URL = "/";

    //定义web应用初始化servlet的名称
    public static final String WEB_INITIALIZER_SERVLET_NAME = "dispatcherServlet";

    //定义web应用初始化servlet的映射路径
    public static final String WEB_INITIALIZER_SERVLET_MAPPING = "/";

    //定义web应用初始化DRUID_SERVLET的名称
    public static final String WEB_INITIALIZER_DRUID_SERVLET_NAME = "druidStatView";

    //定义web应用初始化DRUID_SERVLET的映射路径
    public static final String WEB_INITIALIZER_DRUID_SERVLET_MAPPING = "/druid/*";

    //定义WEB应用中shiro过滤器的名称
    public static final String WEB_INITIALIZER_SHIRO_FILTER = "shiroDelegatingFilterProxy";

    //APP_CONFIG配置文件基本扫描路径,用于扫描service注解的相关路径
    public static final String APP_CONFIG_SERVICE_BASE_PACKAGES = "com.**.service";

    //应用程序基本配置文件路径
    public static final String APPLICATION_CONFIG_PROPERTY_SOURCE = "classpath:/config/application.properties";

    //数据库配置信息扫描路径
    public static final String DATABASE_CONFIG_PACKAGE_TO_SCAN = "com.sample.model";

    //SPRING_MVC扫描基本路径，spring mvc 自动扫描注解的时候，不去扫描@Service,mvc层只负责扫描@Controller
    public static final String SPRING_MVC_CONFIG_BASE_PACKAGES = "com.**.controller";

    //切入点配置基本路径
    public static final String POINTCUT_CONFIG_BASE_PACKAGES = "com.sample.project.aop";

    //定义切面表达式
    public static final String POINTCUT_ASPECT_POINT_CUT = "execution(* com.**.service..*(..))";

    //定义切面执行方法
    public static final String POINTCUT_ADVICE_METHOD = "com.sample.project.aop.PointcutsDefinition.inServiceLayer()";

    //定义SPRING_MVC中页面解析前缀
    public static final String SPRING_MVC_CONFIG_PREFIX = "/WEB-INF/";

    //定义SPRING_MVC中页面解析后缀
    public static final String SPRING_MVC_CONFIG_SUFFIX = ".jsp";

    //定义SPRING_MVC中字符转换器text类型
    public static final String SPRING_MVC_CONFIG_TEXT = "text";

    //定义SPRING_MVC中字符转换器plan类型
    public static final String SPRING_MVC_CONFIG_PLAN = "plan";

    //定义SPRING_MVC中字符转换器html类型
    public static final String SPRING_MVC_CONFIG_HTML = "html";

    //定义MybatisConfig中所指定的mapper文件的路径
    public static final String MYBATIS_BASE_PACKAGES = "com.**.dao.**.mapper";

    //定义MybatisConfig中所指定的实体类文件的路径
    public static final String MYBATIS_TYPE_ALIASES_PACKAGE = "com.**.domain.mybatis";

    //定义数据库初始化文件路径
    public static final String DATASOURCE_INIT_CLASS_PATH_RESOURCE = "db/user.sql";


    //定义PersistenceConfig中所指定的mapper文件的路径
    public static final String JPA_BASE_PACKAGES = "com.**.repositories";

    //定义MybatisConfig中所指定的实体类文件的路径
    public static final String JPA_PACKAGES_TO_SCAN = "com.august.**.domain.**";

    //任务调度信息中集合名称，用于获取任务信息
    public static final String SCHEDULE_JOB_NAME = "scheduleJob";

    //SPRING_MVC中配置国际化资源缓存时间
    public static final int SPRING_MVC_CONFIG_CACHE_SECONDS = 60;
    //SPRING_MVC配置文件中国际化资源基本名称
    public static final String SPRING_MVC_CONFIG_BASE_NAMES = "config.messages";
    //CACHING_CONFIG文件中配置缓存路径
    public static final String CACHING_CONFIG_CLASSPATH = "config/ehcache.xml";
}
