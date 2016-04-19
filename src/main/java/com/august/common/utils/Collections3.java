package com.august.common.utils;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.context.annotation.Description;

import java.util.*;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.common.utils
 * Author: August
 * Update: August(2016/4/13)
 * Description:Collections工具集
 * 在JDK的Collections和Guava的Collections2之后，命名Collections3
 */
@Description("Collections工具集")
public class Collections3 {

    /**
     * 提取集合中的对象的两个属性（通过getter函数、）组合成map
     *
     * @param collection        来源集合
     * @param keyPropertyName   要提取为map的key值的属性名
     * @param valuePropertyName 要提取为map的value值的属性名
     * @return 获取到的集合信息
     */
    public static Map extractToMap(final Collection collection, final String keyPropertyName, final String valuePropertyName) {
        //创建一个跟传入的集合同长度的map集合
        Map map = new HashMap(collection.size());
        for (Object object : collection) {
            try {
                //提取集合中的对象的两个属性（通过getter函数、）组合成map
                map.put(PropertyUtils.getProperty(object, keyPropertyName), PropertyUtils.getProperty(object, valuePropertyName));
            } catch (Exception e) {
                throw Reflections.convertReflectionExceptionToUnchecked(e);
            }
        }
        return map;
    }

    public static List extractToList(final Collection collection, final String propertyName) {
        List list = new ArrayList(collection.size());
        for (Object object : collection) {
            try {
                list.add(PropertyUtils.getProperty(object, propertyName));
            } catch (Exception e) {
                throw Reflections.convertReflectionExceptionToUnchecked(e);
            }
        }
        return list;
    }

    /**
     * 提取集合中的对象的一个属性，组合成有分隔符分隔的字符串
     *
     * @param collection   来源集合
     * @param propertyName 要提取的属性名
     * @param separator    分隔符
     * @return 拼接好的字符串
     */
    public static String extractToString(final Collection collection, String propertyName, String separator) {
        List list = extractToList(collection, propertyName);
        return StringUtils.join(list, separator);
    }


}






















