package com.august.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.config
 * Author: August
 * Update: August(2015/8/23)
 * Description:
 * 配置spring相关内容
 */
@Configuration
////spring 自动扫描注解的时候，不去扫描@Controller
//@ComponentScan(basePackages = "com.august", useDefaultFilters = false,
//        includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, value = {Controller.class})
//        })
public class SpringConfig {
}
