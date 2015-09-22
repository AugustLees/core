package com.august.config;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.august.controller.UserController;
import com.august.utils.StaticConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.SocketUtils;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.util.IntrospectorCleanupListener;
import org.springframework.web.util.Log4jConfigListener;

import javax.servlet.*;
import java.util.EnumSet;


/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.config
 * Author: August
 * Update: August(2015/8/23)
 * Description:web.xml通用设置
 * 配置相关listener，servlet,filter等等
 * 该文件用于替换web.xml文件中的配置信息
 * 应用系统启动时，程序自动加载WebApplicationInitializer的实现类，
 * 故此类应为应用程序自动自动加载，无需特殊处理
 * 此处为直接实现WebApplicationInitializer
 * 在实际应用中，也可以通过如下方式进行处理
 * AbstractSecurityWebApplicationInitializer SpringSecurity相关配置
 */
public class WebApplicationInitialized implements WebApplicationInitializer {
    //定义日志记录器
    private static final Logger LOGGER = LoggerFactory.getLogger(WebApplicationInitialized.class);

    //实现onStartup方法，使程序在启动时主动加载该配置下的文件
    public void onStartup(ServletContext servletContext) throws ServletException {
        LOGGER.debug("WebApplicationInitializer initializer……");
        System.out.println("WebApplicationInitializer initializer……");

        /**
         * 开始加载web.xml文件中配置信息
         * 通过注解的方式实现所有配置文件信息的注入
         * 此部分相当于web.xml文件中
         * <context-param>
         *     <description>配置全局上下文内容</description>
         *     <param-name>contextConfigLocation</param-name>
         *     <param-value>classpath*:spring.xml</param-value>
         * </context-param>
         */
        /*****************************1.加载非web层组件*********************************************/

        //首先创建一个注解配置web引用上下文信息实例，
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.setDisplayName(StaticConstant.WEB_INITIALIZER_DISPLAY_NAME);
        //实现配置信息的注册，此处为配置全局上下文内容
        rootContext.register(AppConfig.class);
        //容器将传入的一个ServletContext(上下文)放入web应用中,这个WEB项目所有部分都将共享这个上下文
        rootContext.setServletContext(servletContext);
        /*********************注册添加监听器相关*************************************************/

        /**
         * 第一部分：以下这段为添加listener部分
         */
        //1、使用实例化的rootContext将创建一个上下文加载监听器
        // ，并将全局的配置信息加载到servlet上下文中
        //即启动Web容器时，自动装配ApplicationContext的配置信息
        servletContext.addListener(new ContextLoaderListener(rootContext));
        System.out.println("1.1、使用实例化的rootContext将创建一个上下文加载监听器，并将全局的配置信息加载到servlet上下文中……");

        //2、添加Logger4j相关配置
        //首先加载log4j的相关配置文件信息
        servletContext.setInitParameter(StaticConstant.WEB_INITIALIZER_LOG4J_CONFIG_LOCATION,
                StaticConstant.WEB_INITIALIZER_LOG4J_CONFIG_LOCATION_PATH);
        //Spring默认刷新Log4j配置文件的间隔,单位为millisecond
        servletContext.setInitParameter(StaticConstant.WEB_INITIALIZER_LOG4J_REFRESH_INTERVAL,
                StaticConstant.WEB_INITIALIZER_LOG4J_REFRESH_INTERVAL_TIME);
        //增加log4j的监听器
        servletContext.addListener(Log4jConfigListener.class);
        System.out.println("1.2、添加Logger4j相关配置……");

        //3、主要负责处理由JavaBean introspector使用而引起的缓冲泄露
        //创建一个防止内存溢出的监听器
        IntrospectorCleanupListener introspectorCleanupListener = new IntrospectorCleanupListener();
        //将监听器添加到上下文中
        servletContext.addListener(introspectorCleanupListener);
        System.out.println("1.3、主要负责处理由JavaBean introspector使用而引起的缓冲泄露……");


        /*********************注册添加过滤器相关*************************************************/

        /**
         * 第二部分：以下这段为添加filter部分
         */

        //1.配置字符集过滤器部分
        //配置字符集过滤器
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        //允许强制转换为UTF-8编码
        characterEncodingFilter.setForceEncoding(true);
        //设定初始化字符集编码
        characterEncodingFilter.setEncoding(StaticConstant.WEB_INITIALIZER_CHARACTER_ENCODING);
        //将该过滤器添加到servlet上下文中并返回一个动态的过滤器注册机制
        FilterRegistration.Dynamic encodingFilter = servletContext.addFilter(StaticConstant.WEB_INITIALIZER_CHARACTER_ENCODING_FILTER,
                characterEncodingFilter);
        //对配置好的字符集编码进行URL的映射
        encodingFilter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE), false,
                StaticConstant.WEB_INITIALIZER_CHARACTER_ENCODING_URL);

        System.out.println("2.1、配置字符集过滤器部分……");

        //2、启用druidDatasource的web监控功能
        WebStatFilter webStatFilter = new WebStatFilter();
        webStatFilter.isExclusion(StaticConstant.WEB_INITIALIZER_IS_EXCLUSION);
        FilterRegistration.Dynamic druidWebStatFilter = servletContext.addFilter(StaticConstant.WEB_INITIALIZER_DRUID_WEB_STAT_FILTER,
                webStatFilter);
        druidWebStatFilter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE), false,
                StaticConstant.WEB_INITIALIZER_DRUID_WEB_STAT_FILTER_URL);

        System.out.println("2.2、配置启用druidDatasource的web监控功能过滤器部分……");


        /*********************注册添加Servlet相关*************************************************/

        /**
         * 创建Spring view分发器
         * Spring会创建一个WebApplicationContext上下文，称为父上下文（父容器），
         * 保存在 ServletContext中，key是WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE的值。
         * 可以使用Spring提供的工具类取出上下文对象：WebApplicationContextUtils.getWebApplicationContext(ServletContext);
         * DispatcherServlet是一个Servlet,可以同时配置多个，
         * 每个 DispatcherServlet有一个自己的上下文对象（WebApplicationContext），称为子上下文（子容器），
         * 子上下文可以访问父上下文中的内容,但父上下文不能访问子上下文中的内容。
         * 它也保存在 ServletContext中，key是"org.springframework.web.servlet.FrameworkServlet.CONTEXT"+Servlet名称。
         * 当一个Request对象产生时，会把这个子上下文对象（WebApplicationContext）保存在Request对象中，
         * key是DispatcherServlet.class.getName() + ".CONTEXT"。
         * 可以使用工具类取出上下文对象：RequestContextUtils.getWebApplicationContext(request);
         */
        /**
         * 第三部分：以下部分为添加servlet部分
         */

        /**
         * 配置springMvc的servlet,用于拦截请求
         * 在servlet上下文中，添加路径访问限制配置信息
         */
        //1、配置springMvc的servlet，
        //首先实例化一个dispatcher,并添加到servlet上下文中
        DispatcherServlet dispatcherServlet = new DispatcherServlet(rootContext);
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet(StaticConstant.WEB_INITIALIZER_SERVLET_NAME,
                dispatcherServlet);
        //设定加载顺序
        dispatcher.setLoadOnStartup(1);
        //设定拦截路径
        dispatcher.addMapping(StaticConstant.WEB_INITIALIZER_SERVLET_MAPPING);
        System.out.println("3.1、配置springMvc的servlet……");

        //2、启用druidDatasource的web监控功能
        ServletRegistration.Dynamic dynamic = servletContext.addServlet(StaticConstant.WEB_INITIALIZER_DRUID_SERVLET_NAME,
                new StatViewServlet(  ));
        dynamic.addMapping(StaticConstant.WEB_INITIALIZER_DRUID_SERVLET_MAPPING);
        //访问监控页面：http://ip：port/projectName/druid/index.html
        System.out.println("3.2、配置启用druidDatasource的web监控功能的servlet……");


    }
}
