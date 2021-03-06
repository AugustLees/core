package com.august.common;

import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.modules.system.utils
 * Author: August
 * Update: August(2015/9/19)
 * Description:配置Mybatis管理的事务控制器
 * 当以@MybatisTxt注入时，自动启用mybatis事务控制器
 * 当出现异常时，进行事务回滚，保证事务的一致性
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Transactional(transactionManager = "myBatisTransactionManager",rollbackFor={Exception.class})
public @interface MyBatisTx {
}
