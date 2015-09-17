package com.august.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.config.AnnotationDrivenBeanDefinitionParser;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.text.SimpleDateFormat;
import java.util.Arrays;

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
@ComponentScan(basePackages = "com.august.controller", useDefaultFilters = false,
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, value = {Service.class})
        }, includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, value = {Controller.class})})
//启动Spring MVC的注解功能，完成请求和注解POJO的映射
public class MVCConfig extends WebMvcConfigurerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(MVCConfig.class);

    //控制日期格式的
    @Bean(name = "conversionService")
    public FormattingConversionServiceFactoryBean formattingConversionServiceFactoryBean() {
        LOGGER.debug("MVC CONFIG 中注册日期格式转换服务器……");
        System.out.println("MVC CONFIG 中注册日期格式转换服务器……");
        return new FormattingConversionServiceFactoryBean();
    }

    /**
     * 对模型视图名称的解析，即在模型视图名称添加前后缀
     *
     * @return
     */
    @Bean
    public InternalResourceViewResolver internalResourceViewResolver() {
        LOGGER.debug("MVC CONFIG 中对模型视图名称的解析，即在模型视图名称添加前后缀……");
        System.out.println("MVC CONFIG 中对模型视图名称的解析，即在模型视图名称添加前后缀……");
        InternalResourceViewResolver internalResourceViewResolver = new InternalResourceViewResolver();
        internalResourceViewResolver.setPrefix("/WEB-INF/");
        internalResourceViewResolver.setSuffix("*.jsp");
        return internalResourceViewResolver;
    }


    /**
     * 设置文件上传管理器
     * 上传文件大小限制为100M
     *
     * @return
     */
    @Bean
    public CommonsMultipartResolver commonsMultipartResolver() {
        LOGGER.debug("MVC CONFIG 中设置文件上传管理器……");
        System.out.println("MVC CONFIG中设置文件上传管理器……");
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
        commonsMultipartResolver.setDefaultEncoding("UTF-8");
        commonsMultipartResolver.setMaxUploadSize(10000000L);
        return commonsMultipartResolver;
    }

    @Bean//注册时间各格式器
    public SimpleDateFormat simpleDateFormat() {
        LOGGER.debug("MVC CONFIG 中注册时间各格式器……");
        System.out.println("MVC CONFIG中注册时间各格式器……");
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    @Bean
    public ObjectMapper objectMapper() {
        LOGGER.debug("MVC CONFIG 中将时间格式器注入到mapper对象中……");
        System.out.println("MVC CONFIG中将时间格式器注入到mapper对象中……");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setDateFormat(simpleDateFormat());
        return objectMapper;
    }


    /**
     * 默认情况下MappingJacksonHttpMessageConverter会设置content为application/json，在IE9下返回会出现提示下载的现象
     *
     * @return
     */
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        LOGGER.debug("MVC CONFIG 中注入MappingJackson2HttpMessageConverter组件避免出现下载现象……");
        System.out.println("MVC CONFIG 中注入MappingJackson2HttpMessageConverter组件避免出现下载现象…………");
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setPrefixJson(true);
        mappingJackson2HttpMessageConverter.setObjectMapper(objectMapper());
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED));
        return mappingJackson2HttpMessageConverter;
    }

    @Bean
    public StringHttpMessageConverter stringHttpMessageConverter() {
        LOGGER.debug("MVC CONFIG 中注入stringHttpMessageConverter组件……");
        System.out.println("MVC CONFIG 中注入stringHttpMessageConverter组件…………");
        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
        stringHttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.TEXT_HTML, MediaType.TEXT_PLAIN));
        return stringHttpMessageConverter;
    }

    @Bean
    public AnnotationDrivenBeanDefinitionParser annotationDrivenBeanDefinitionParser() {
        LOGGER.debug("MVC CONFIG 中开启MVC注解驱动……");
        System.out.println("MVC CONFIG 中中开启MVC注解驱动…………");
        AnnotationDrivenBeanDefinitionParser annotationDrivenBeanDefinitionParser = new AnnotationDrivenBeanDefinitionParser();
        return annotationDrivenBeanDefinitionParser;
    }

    //RequestMappingHandlerAdapter需要显示声明，否则不能注册通用属性编辑器
    @Bean
    public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
        RequestMappingHandlerAdapter requestMappingHandlerAdapter = new RequestMappingHandlerAdapter();
        requestMappingHandlerAdapter.setMessageConverters(Arrays.<HttpMessageConverter<?>>asList(mappingJackson2HttpMessageConverter()));
        return requestMappingHandlerAdapter;
    }

    /**
     * 会自动注册RequestMappingHandlerMapping与RequestMappingHandlerAdapter
     * 两个bean,是spring MVC为@Controllers分发请求所必须的。
     * 并提供了：数据绑定支持，@NumberFormatannotation支持，
     * 和@DateTimeFormat 支持，@Valid支持，读写XML的支持（JAXB），读写JSON的支持（Jackson）
     *
     * @return
     */
    @Bean
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        LOGGER.debug("MVC CONFIG 中 RequestMappingHandlerMapping");
        //RequestMappingHandlerMapping需要显示声明，否则不能注册自定义的拦截器>
        return new RequestMappingHandlerMapping();
    }
}
