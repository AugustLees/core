package com.august.common.utils;

import org.springframework.context.annotation.Description;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.common.utils
 * Author: August
 * Update: August(2016/4/18)
 * Description:关于异常的工具类
 */
@Description("关于异常的工具类")
public class Exceptions {

    /**
     * 将checkException转换为UncheckedException
     *
     * @param exception 异常信息
     * @return 转换后的异常信息
     */
    public static RuntimeException unchecked(Exception exception) {
        if (exception instanceof RuntimeException) return (RuntimeException) exception;
        return new RuntimeException(exception);
    }

    /**
     * 将ErrorStack转换String
     *
     * @param throwable 可抛出异常
     * @return 异常信息字符串
     */
    public static String getStackTraceAsString(Throwable throwable) {
        if (throwable == null) return "";
        StringWriter stringWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    /**
     * 在request中获取异常类
     *
     * @param request 请求数据
     * @return 异常信息
     */
    public static Throwable getThrowable(HttpServletRequest request) {
        Throwable throwable = null;
        if (request.getAttribute("exception") != null)
            throwable = (Throwable) request.getAttribute("exception");
        else if (request.getAttribute("javax.servlet.error.request") != null) {
            throwable = (Throwable) request.getAttribute("javax.servlet.error.request");
        }
        return throwable;
    }
}
