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
 * Description:配置JPA管理的事务控制器
 * 当有@JPATx注解时，会自动注入该事务
 * 当出现异常时，进行事务回滚，保证事务的一致性
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
//声明使用事务，不声明spring会使用默认事务管理
@Transactional(transactionManager = "JPA", rollbackFor={Exception.class})
public @interface JPATx {
}
