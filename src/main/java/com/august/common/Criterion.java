package com.august.common;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.common
 * Author: Administrator
 * Update: Administrator(2015/10/10)
 * Description:条件接口
 * 用户提供条件表达式接口
 */
public interface Criterion {
    enum Operator {
        EQ, NE, LIKE, GT, LT, GTE, LTE, AND, OR
    }

    Predicate toPredicate(Root<?> root, CriteriaQuery<?> query,
                          CriteriaBuilder builder);
}