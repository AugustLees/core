package com.august.common;

import com.august.domain.JobSchedule;
import com.august.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.utils
 * Author: August
 * Update: August(2015/9/27)
 * Description:任务调度工具类
 */
public class JobScheduleUtils {
    public static final Logger LOGGER = LoggerFactory.getLogger(JobScheduleUtils.class);

    /**
     * 通过反射调用JobSchedule中定义的执行方法
     *
     * @param scheduleJob 需要进行反射的实体信息
     */
    public static void invokeMethod(JobSchedule scheduleJob) {
        Object object = null;
        Class clazz = null;
        //如果实体信息中存在springID则根据springID来获取对应的bean实例信息
        if (!StringUtils.isEmpty(scheduleJob.getSpringId())) {
            object = SpringUtils.getBean(scheduleJob.getSpringId());
        } else if (!StringUtils.isEmpty(scheduleJob.getBeanClass())) {
            //如果scheduleJob中bean类信息存在，则反射未对应的类信息并实例化
            try {
                clazz = Class.forName(scheduleJob.getBeanClass());
                object = clazz.newInstance();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        //保证实例化信息存在
        if (object == null) {
            LOGGER.error("任务名称 = [{}]---------------未启动成功，请检查是否配置正确！！！", scheduleJob.getJobName());
        }

        //获取类对象信息
        clazz = object.getClass();
        Method method = null;
        try {
            //根据方法名查找声明的方法信息
            method = clazz.getDeclaredMethod(scheduleJob.getMethodName());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            LOGGER.error("任务名称 = [{}]---------------未启动成功，方法名设置错误！！！", scheduleJob.getJobName());
        }
        //如果方法信息存在则进行反射
        if (method != null) {
            try {
                method.invoke(object);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        LOGGER.info("任务名称 = [{}]----------启动成功", scheduleJob.getJobName());
    }
}
