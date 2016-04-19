package com.august.common.config;

import com.august.common.SpringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.common.config
 * Author: August
 * Update: August(2015/9/24)
 * Description:任务调度器配置
 * 用于配置调度器相关信息，此处的任务调度管理为动态指定的管理方式
 */
@Configuration
public class SchedulerConfig {
    //初始化spring工具类
    @Bean
    public SpringUtils springUtils() {
        return new SpringUtils();
    }

    //配置任务调度管理器主程序
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(){
        return new SchedulerFactoryBean();
    }
}
