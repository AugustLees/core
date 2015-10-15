package com.august.common;

import com.august.service.util.Criterion;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.utils
 * Author: Administrator
 * Update: Administrator(2015/10/10)
 * Description: 定义一个查询条件容器
 */
public class Criteria1<T> implements Specification<T> {
    private List<Criterion> criterionList = new ArrayList<Criterion>();

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query,
                                 CriteriaBuilder builder) {


        if (!criterionList.isEmpty()) {
            List<Predicate> predicates = new ArrayList<Predicate>();
            for (Criterion criterion : criterionList) {
                predicates.add(criterion.toPredicate(root, query, builder));
            }
            // 将所有条件用 and 联合起来
            if (predicates.size() > 0) {
                return builder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        }
        return builder.conjunction();
    }

    /**
     * 增加简单条件表达式
     */
    public void add(Criterion criterion) {
        if (criterion != null) {
            criterionList.add(criterion);
        }
    }
}
