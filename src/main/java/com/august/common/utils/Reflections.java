package com.august.common.utils;

import org.springframework.context.annotation.Description;

import java.lang.reflect.InvocationTargetException;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.common.utils
 * Author: August
 * Update: August(2016/4/13)
 * Description:反射工具类
 * 提供调用getter/setter 方法，访问私有变量，调用私有方法，获取泛型类型class，被AOP过的真实类等工具函数
 */
@Description("反射工具类")
public class Reflections {

    private static final String SETTER_PREFIX = "set";

    private static final String GETTER_PREFIX = "get";

    private static final String CGLIB_CLASS_SEPARATOR = "$$";

    /**
     * 将反射时的checked exception 转换为un
     *
     * @param e 异常信息
     * @return 转换后的异常信息
     */
    public static RuntimeException convertReflectionExceptionToUnchecked(Exception e) {
        if (e instanceof IllegalAccessException
                || e instanceof IllegalArgumentException
                || e instanceof NoSuchMethodException)
            return new IllegalArgumentException(e);
        else if (e instanceof InvocationTargetException)
            new RuntimeException(((InvocationTargetException) e).getTargetException());
        else if (e instanceof RuntimeException)
            return (RuntimeException) e;
        return new RuntimeException("Unexpected Checked Exception.", e);
    }
}






















