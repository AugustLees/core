package com.august.service.util;

import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import java.util.List;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.service.util
 * Author: Administrator
 * Update: Administrator(2015/10/14)
 * Description: 定义简单条件表达式信息
 */
public class SimpleExpression implements Criterion {
    private String propertyName;    //属性名
    private Object value;           //属性值
    private boolean ignoreCase;     //是否忽略大小写
    private Operator operator;      //操作信息

    /**
     * 实例化条件表达式信息
     *
     * @param propertyName 属性名
     * @param value        属性值
     * @param operator     操作信息
     */
    public SimpleExpression(String propertyName, Object value, Operator operator) {
        this.propertyName = propertyName;
        this.value = value;
        this.operator = operator;
    }

    /**
     * 实例化条件表达式信息
     *
     * @param propertyName 属性名
     * @param value        属性值
     * @param ignoreCase   是否忽略大小写
     * @param operator     操作信息
     */
    public SimpleExpression(String propertyName, Object value, boolean ignoreCase, Operator operator) {
        this.propertyName = propertyName;
        this.value = value;
        this.ignoreCase = ignoreCase;
        this.operator = operator;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    public void setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    /**
     *
     * @param root
     * @param criteriaQuery
     * @param criteriaBuilder
     * @return
     */
    @Override
    public Predicate toPredicate(Root<?> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        Predicate predicate = criteriaBuilder.conjunction();
        javax.persistence.criteria.Path expression = null;
        //如果存在子属性，即a.b这样的属性，则需要拆分进行重组
        if (propertyName.contains(".")) {
            String[] names = StringUtils.split(propertyName, ".");
            //首先根据父属性获取对应的表达式信息
            expression = root.get(names[0]);
            //然后通过循环获取该表达式信息的自表达式信息
            for (String name : names) {
                expression = expression.get(name);
            }
        } else {
            //如果不存在子属性，则直接获取对应的属性信息
            expression = root.get(propertyName);
        }
        //如果忽略大小写则将值忽略大小写
        if (ignoreCase) {
            value = this.value.toString().toLowerCase();
        }
        switch (operator) {
            case EQ://如果是等于则返回等于的操作
                return criteriaBuilder.equal(expression, value);
            case NE:
                return criteriaBuilder.notEqual(expression, value);
            case LIKE:
                return criteriaBuilder.like((Expression<String>) expression, "%" + value + "%");
            case LT:
                return criteriaBuilder.lessThan(expression, (Comparable) value);
            case GT:
                return criteriaBuilder.greaterThan(expression, (Comparable) value);
            case LTE:
                return criteriaBuilder.lessThanOrEqualTo(expression, (Comparable) value);
            case GTE:
                return criteriaBuilder.greaterThanOrEqualTo(expression, (Comparable) value);
            case AND:
//                return criteriaBuilder.and(expression, (Expression<Boolean>) value);
            case OR:
//                return criteriaBuilder.or(expression, (Expression<Boolean>) value);
            default:
                return null;
        }
    }
}
