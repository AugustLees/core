package com.august.config;

import com.august.utils.StaticConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.format.FormatterRegistrar;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.datetime.DateFormatterRegistrar;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.config
 * Author: August
 * Update: August(2015/8/23)
 * Description:配置springMVC相关内容
 */
@Configuration
@EnableWebMvc//允许开启WEB mvc模式
//扫描基本包路径  spring mvc 自动扫描注解的时候，不去扫描@Service,mvc层只负责扫描@Controller
@ComponentScan(basePackages = StaticConstant.SPRING_MVC_CONFIG_BASE_PACKAGES,
        useDefaultFilters = false,
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, value = {Service.class})},
        includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, value = {Controller.class})}
)
//启动Spring MVC的注解功能，完成请求和注解POJO的映射
public class MVCConfig extends WebMvcConfigurerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(MVCConfig.class);

    /**
     * 对模型视图名称的解析，即在模型视图名称添加前后缀
     *
     * @return
     */
    @Bean
    public InternalResourceViewResolver internalResourceViewResolver() {
        LOGGER.debug("MVC CONFIG 中 8、对模型视图名称的解析，即在模型视图名称添加前后缀……");
        InternalResourceViewResolver internalResourceViewResolver = new InternalResourceViewResolver();
        internalResourceViewResolver.setPrefix(StaticConstant.SPRING_MVC_CONFIG_PREFIX);
        internalResourceViewResolver.setSuffix(StaticConstant.SPRING_MVC_CONFIG_SUFFIX);
        internalResourceViewResolver.setViewClass(JstlView.class);
        return internalResourceViewResolver;
    }


    /**
     * 定义国际化资源文件查找规则 ，各种messages.properties
     *
     * @return
     */
    @Bean
    public ResourceBundleMessageSource resourceBundleMessageSource() {
        LOGGER.debug("MVC CONFIG 中 7、配置国际化资源文件信息……");
        ResourceBundleMessageSource resourceBundleMessageSource = new ResourceBundleMessageSource();
        resourceBundleMessageSource.setBasenames(new String[]{StaticConstant.SPRING_MVC_CONFIG_BASE_NAMES});
        resourceBundleMessageSource.setDefaultEncoding(StaticConstant.CHARACTER);
        resourceBundleMessageSource.setCacheSeconds(StaticConstant.SPRING_MVC_CONFIG_CACHE_SECONDS);
        return resourceBundleMessageSource;
    }

    /**
     * 设置文件上传管理器
     * 上传文件大小限制为100M
     *
     * @return
     */
    @Bean
    public CommonsMultipartResolver commonsMultipartResolver() {
        LOGGER.debug("MVC CONFIG 中 6、设置文件上传管理器……");
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
        commonsMultipartResolver.setDefaultEncoding(StaticConstant.CHARACTER);
        commonsMultipartResolver.setMaxUploadSize(10000000L);
        return commonsMultipartResolver;
    }


    /**
     * 如果项目的一些资源文件放在/WEB-INF/resources/下面
     * 在浏览器访问的地址就是类似：http://host:port/projectName/WEB-INF/resources/xxx.css
     * 但是加了如下定义之后就可以这样访问：
     * http://host:port/projectName/resources/xxx.css
     * 非必须
     */
    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/WEB-INF/resources/").setCachePeriod(31556926);
        registry.addResourceHandler("/base/**").addResourceLocations("/WEB-INF/base/");
    }


    /**
     * 基于cookie的本地化资源处理器
     *
     * @return
     */
    @Bean(name = "localeResolver")
    public CookieLocaleResolver cookieLocaleResolver() {
        LOGGER.debug("MVCConfig 中 5、基于cookie的本地化资源处理器 CookieLocaleResolver");
        return new CookieLocaleResolver();
    }

    /**
     * 添加拦截器
     *
     * @param registry 拦截器注册表
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LOGGER.debug("MVCConfig 中 1、添加拦截器 addInterceptors start");
        registry.addInterceptor(new LocaleChangeInterceptor());
        LOGGER.debug("MVCConfig 中 1、添加拦截器 addInterceptors end");
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(stringHttpMessageConverter());
        converters.add(mappingJackson2HttpMessageConverter());
    }


    /**
     * 默认情况下MappingJacksonHttpMessageConverter会设置content为application/json，在IE9下返回会出现提示下载的现象
     *
     * @return
     */
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        LOGGER.debug("MVC CONFIG 中 4、注入MappingJackson2HttpMessageConverter组件避免出现下载现象……");
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setPrettyPrint(true);
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED));
        return mappingJackson2HttpMessageConverter;
    }

    public StringHttpMessageConverter stringHttpMessageConverter() {
        LOGGER.debug("MVC CONFIG 中 3、注入stringHttpMessageConverter组件……");
        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter(Charset.forName(StaticConstant.CHARACTER));
        stringHttpMessageConverter.setWriteAcceptCharset(Charset.isSupported(StaticConstant.CHARACTER));
        stringHttpMessageConverter.setSupportedMediaTypes(Arrays.asList(
                new MediaType(StaticConstant.SPRING_MVC_CONFIG_TEXT,
                        StaticConstant.SPRING_MVC_CONFIG_PLAN,
                        Charset.forName(StaticConstant.CHARACTER)),
                new MediaType(StaticConstant.SPRING_MVC_CONFIG_TEXT,
                        StaticConstant.SPRING_MVC_CONFIG_HTML,
                        Charset.forName(StaticConstant.CHARACTER))
        ));
        return stringHttpMessageConverter;
    }
}

/**
 * <!-- 启用注解，并定义组件查找规则 ，mvc层只负责扫描@Controller -->
 * <context:component-scan base-package="com.**.controller" use-default-filters="false">
 * <context:include-filter type="annotation" expression="org.springframework.stereotype.Controller" />
 * <context:exclude-filter type="annotation" expression="org.springframework.stereotype.Service" />
 * </context:component-scan>
 * <!-- 视图处理器 -->
 * <bean id="viewResolver" class="org.springframework.web.servlet.view.InternalResourceViewResolver">
 * <property name="prefix" value="/WEB-INF/views/jsp/function/" />
 * <property name="suffix" value=".jsp" />
 * </bean>
 * <!-- 定义国际化资源文件查找规则 ，各种messages.properties -->
 * <bean id="messageSource" class="org.springframework.context.support.ResourceBundleMessageSource" >
 * <property name="basenames">
 * <list>
 * <!-- 在web环境中一定要定位到classpath 否则默认到当前web应用下找  -->
 * <value>classpath:config.messages</value>
 * </list>
 * </property>
 * <property name="defaultEncoding" value="UTF-8"/>
 * <property name="cacheSeconds" value="60"/>
 * </bean>
 * <p>
 * <!-- servlet适配器，这里必须明确声明，因为spring默认没有初始化该适配器 -->
 * <bean id="servletHandlerAdapter"
 * class="org.springframework.web.servlet.handler.SimpleServletHandlerAdapter" />
 * <p>
 * <!-- 定义文件上传处理器 -->
 * <bean id="multipartResolver"
 * class="org.springframework.web.multipart.commons.CommonsMultipartResolver"
 * p:defaultEncoding="UTF-8" />
 * <p>
 * <!-- 异常处理器 -->
 * <bean id="exceptionResolver" class="web.core.CP_SimpleMappingExceptionResolver">
 * <property name="defaultErrorView" value="common_error" />
 * <property name="exceptionAttribute" value="exception" />
 * <property name="exceptionMappings">
 * <props>
 * <prop key="java.lang.RuntimeException">common_error</prop>
 * </props>
 * </property>
 * </bean>
 * <p>
 * <p>
 * <!-- 定义公共参数初始化拦截器 -->
 * <bean id="initInterceptor" class="web.core.CP_InitializingInterceptor" />
 * <p>
 * <p>
 * <p>
 * <p>
 * <!-- 本地化资源处理器 -->
 * <bean id="localeResolver"
 * class="org.springframework.web.servlet.i18n.CookieLocaleResolver" />
 * <p>
 * <!-- 定义本地化变更拦截器 -->
 * <bean id="localeChangeInterceptor"
 * class="org.springframework.web.servlet.i18n.LocaleChangeInterceptor" />
 * <p>
 * <p>
 * <!-- 请求拦截器，每一个用户请求都会被拦截 -->
 * <mvc:interceptors>
 * <ref bean="localeChangeInterceptor" />
 * <ref bean="initInterceptor" />
 * </mvc:interceptors>
 * <p>
 * <p>
 * <p>
 * <p>
 * <!-- 定义注解驱动Controller方法处理适配器 ,注：该适配器必须声明在<mvc:annotation-driven />之前，否则不能正常处理参数类型的转换 -->
 * <bean
 * class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter">
 * <property name="webBindingInitializer">
 * <bean class="web.core.CP_PropertyEditorRegistrar">
 * <property name="format" value="yyyy-MM-dd"></property>
 * </bean>
 * </property>
 * <property name="messageConverters">
 * <list>
 * <bean
 * class="org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter" />
 * <bean
 * class="org.springframework.http.converter.json.MappingJackson2HttpMessageConverter" />
 * </list>
 * </property>
 * </bean>
 * <p>
 * <p>
 * <!-- 会自动注册RequestMappingHandlerMapping与RequestMappingHandlerAdapter
 * 两个bean,是spring MVC为@Controllers分发请求所必须的。 并提供了：数据绑定支持，@NumberFormatannotation支持，@DateTimeFormat支持，@Valid支持，读写XML的支持（JAXB），读写JSON的支持（Jackson） -->
 * <mvc:annotation-driven />
 * <p>
 * <p>
 * <!-- 资源访问处理器 -->
 * <mvc:resources mapping="/static/**" location="/WEB-INF/static/" />
 */