package com.august.config;

import com.august.utils.StaticConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.stereotype.Controller;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.config
 * Author: August
 * Update: August(2015/8/23)
 * Description:配置全局上下文内容
 * 替换原来的配置文件application.xml文件
 * Spring3.1基于注解的配置类， 用于代替原来的<b>applicationContext.xml</b>配置文件
 */
@Configuration//表示该类是一个配置文件 //标注此类为配置类（必有）
//指定扫描路径信息,如果多个，用“,”分隔，相当于<context:component-scan base-package="com.handu.base">
// <context:exclude-filter type="annotation"
//        expression="org.springframework.stereotype.Controller"/>
// </context:component-scan>
@ComponentScan(basePackages = {StaticConstant.APP_CONFIG_SERVICE_BASE_PACKAGES},
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, value = {Controller.class})}
)
//引入配置信息
//引入相关程序的配置文件
@Import({MVCConfig.class, JavaMailConfig.class,/* AopConfig.class,*/MyBatisConfig.class, PersistenceConfig.class, SchedulerConfig.class})
//加载资源文件
@PropertySource(value = StaticConstant.APPLICATION_CONFIG_PROPERTY_SOURCE)
public class AppConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppConfig.class);

    /**
     * 实例化资源文件解析bean
     * 这个类必须有，而且必须声明为static，否则不能正常解析
     * spring启动时扫描项目路径下的properties文件,后续用${key }方式取出对应值,这样可以代码解耦和，后续只需修改properties文件即可
     * 等同于@PropertySource
     *
     * @return
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        LOGGER.debug("AppConfig中PropertySourcesPlaceholderConfigurer initializer……");
        System.out.println("AppConfig中PropertySourcesPlaceholderConfigurer initializer……");
        PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
//        propertySourcesPlaceholderConfigurer.setLocation(new pr);
        return propertySourcesPlaceholderConfigurer;
    }

}
