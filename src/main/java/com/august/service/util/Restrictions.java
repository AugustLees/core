package com.august.service.util;

import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.MatchMode;
import org.springframework.util.StringUtils;

import java.util.Collection;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.service.util
 * Author: Administrator
 * Update: Administrator(2015/10/14)
 * Description:约束信息构造器
 * 用于创建条件表达式
 */
public class Restrictions {
    /**
     * 等于
     *
     * @param fieldName  匹配字段
     * @param value      匹配值
     * @param ignoreNull 是否忽略空
     * @return 拼接完成的字符串
     */
    public static SimpleExpression equal(String fieldName, Object value, boolean ignoreNull) {
        if (StringUtils.isEmpty(value)) return null;
        return new SimpleExpression(fieldName, value, Criterion.Operator.EQ);
    }

    /**
     * 不等于
     *
     * @param fieldName  匹配字段
     * @param value      匹配值
     * @param ignoreNull 是否忽略空
     * @return 拼接完成的字符串
     */
    public static SimpleExpression notEqual(String fieldName, Object value, boolean ignoreNull) {
        if (StringUtils.isEmpty(value)) return null;
        return new SimpleExpression(fieldName, value, Criterion.Operator.NE);
    }

    /**
     * 模糊匹配
     *
     * @param fieldName  匹配字段
     * @param value      匹配值
     * @param ignoreNull 是否忽略空
     * @return 拼接完成的字符串
     */
    public static SimpleExpression like(String fieldName, String value, boolean ignoreNull) {
        if (StringUtils.isEmpty(value)) return null;
        return new SimpleExpression(fieldName, value, Criterion.Operator.LIKE);
    }

    /**
     * @param fieldName  匹配字段
     * @param value      匹配值
     * @param matchMode  匹配模式
     * @param ignoreNull 是否忽略空
     * @return 拼接完成的字符串
     */
    public static SimpleExpression like(String fieldName, String value,
                                        MatchMode matchMode, boolean ignoreNull) {
        if (StringUtils.isEmpty(value)) return null;
        return new SimpleExpression(fieldName, value, Criterion.Operator.LIKE);
    }

    /**
     * 大于
     *
     * @param fieldName  匹配字段
     * @param value      匹配值
     * @param ignoreNull 是否忽略空
     * @return 拼接完成的字符串
     */
    public static SimpleExpression greaterThan(String fieldName, Object value, boolean ignoreNull) {
        if (StringUtils.isEmpty(value)) return null;
        return new SimpleExpression(fieldName, value, Criterion.Operator.GT);
    }

    /**
     * 小于
     *
     * @param fieldName  匹配字段
     * @param value      匹配值
     * @param ignoreNull 是否忽略空
     * @return 拼接完成的字符串
     */
    public static SimpleExpression lessThan(String fieldName, Object value, boolean ignoreNull) {
        if (StringUtils.isEmpty(value)) return null;
        return new SimpleExpression(fieldName, value, Criterion.Operator.LT);
    }

    /**
     * 大于等于
     *
     * @param fieldName  匹配字段
     * @param value      匹配值
     * @param ignoreNull 是否忽略空
     * @return 拼接完成的字符串
     */
    public static SimpleExpression lessThanEqual(String fieldName, Object value, boolean ignoreNull) {
        if (StringUtils.isEmpty(value)) return null;
        return new SimpleExpression(fieldName, value, Criterion.Operator.GTE);
    }

    /**
     * 小于等于
     *
     * @param fieldName  匹配字段
     * @param value      匹配值
     * @param ignoreNull 是否忽略空
     * @return
     */
    public static SimpleExpression greaterThanEqual(String fieldName, Object value, boolean ignoreNull) {
        if (StringUtils.isEmpty(value)) return null;
        return new SimpleExpression(fieldName, value, Criterion.Operator.LTE);
    }

    /**
     * 并且
     *
     * @param criterions
     * @return
     */
    public static LogicalExpression and(Criterion... criterions) {
//        return new LogicalExpression(criterions, Criteria.Operator.AND);
        return null;
    }

    /**
     * 或者
     *
     * @param criterions
     * @return
     */
    public static LogicalExpression or(Criterion... criterions) {
//        return new LogicalExpression(criterions, Criteria.Operator.OR);
        return null;
    }

    /**
     * 包含于
     *
     * @param fieldName  匹配字段
     * @param value      匹配值
     * @param ignoreNull 是否忽略空
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static LogicalExpression in(String fieldName, Collection value, boolean ignoreNull) {
        if (ignoreNull && (value == null || value.isEmpty())) {
            return null;
        }
        SimpleExpression[] ses = new SimpleExpression[value.size()];
        int i = 0;
        for (Object obj : value) {
            ses[i] = new SimpleExpression(fieldName, obj, Criterion.Operator.EQ);
            i++;
        }
//        return new LogicalExpression(ses, Criteria.Operator.OR);
        return null;
    }
}
