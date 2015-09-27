package com.august.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.utils
 * Author: August
 * Update: August(2015/9/24)
 * Description:定义一个spring的工具类，该类需要实现BeanFactoryPostProcessor，用来
 * 获取Spring的注册的bean
 */
public final class SpringUtils implements BeanFactoryPostProcessor {
    //创建一个可以配置的Bean的工厂列表
    private static ConfigurableListableBeanFactory beanFactory;//Spring应用上下文环境

    /**
     * 重写postProsessBeanFactory方法，用来初始化beanFactory
     *
     * @param configurableListableBeanFactory
     * @throws BeansException
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        SpringUtils.beanFactory = configurableListableBeanFactory;
    }


    /**
     * 根据bean的名字获取bean对象
     *
     * @param name 需要获取bean的名字
     * @param <T>  需要转换的对象的类型
     * @return 容器中存在的bean
     */
    public static <T> T getBean(String name) throws BeansException {
        return (T) beanFactory.getBean(name);
    }

    /**
     * 根据指定的类型获取对应的bean
     * 即获取类型为requiredType的对象
     *
     * @param clazz    需要获取的类型的实例
     * @param <T>具体的类型
     * @return 容器中获取到的bean对象
     */
    public static <T> T getBean(Class<T> clazz) throws BeansException {
        T result = beanFactory.getBean(clazz);
        return result;
    }

    /**
     * 查找在beanFactory中是否存在指定名字的bean
     * 如果BeanFactory包含一个与所给名称匹配的bean定义，则返回true
     *
     * @param name 需要查找的bean的名字
     * @return 查找结果为true或者false
     */
    public static boolean containsBean(String name) {
        return beanFactory.containsBean(name);
    }

    /**
     * 判断以给定名字注册的bean定义是一个单例模式singleton还是一个多例模式prototype
     * 如果与给定的名字相应的bean定义没有找到，将会抛出一个异常为NoSuchBeanDefinitionException
     *
     * @param name 需要查找的bean的名字
     * @return 查找结果为true或者false
     */
    public static boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
        return beanFactory.isSingleton(name);
    }

    /**
     * 根据名字获取类型
     *
     * @param name 需要查询的bean的名字
     * @return 对应的指定类型的bean对象
     */
    public static Class<?> getType(String name) throws NoSuchBeanDefinitionException {
        return beanFactory.getType(name);
    }

    /**
     * 根据所给的名字，去查找名字为给定name的bean的别名
     * 如果存在别名，则返回这些别名
     *
     * @param name 需要查找的bean名字
     * @return 该bean所对应的别名
     */
    public static String[] getAliases(String name) {
        return beanFactory.getAliases(name);
    }
}
