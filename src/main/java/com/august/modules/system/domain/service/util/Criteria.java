package com.august.modules.system.domain.service.util;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.modules.system.domain.service.util
 * Author: Administrator
 * Update: Administrator(2015/10/14)
 * Description:定义约束条件
 */
public class Criteria<T> implements Specification {
    private List<Criterion> criterionList = new ArrayList<Criterion>();

    /**
     * 拼接查询条件构造器
     *
     * @param root
     * @param criteriaQuery
     * @param criteriaBuilder
     * @return
     */
    @Override
    public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
        //如果存在查询条件则
        if (!criterionList.isEmpty()) {
            //生成一个查询条件列表
            List<Predicate> predicateList = new ArrayList<Predicate>();
            for (Criterion criterion : criterionList) {
                predicateList.add(criterion.toPredicate(root, criteriaQuery, criteriaBuilder));
            }
            if (predicateList.size() > 0) {
                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        }
        //将所有查询条件拼接起来
        return criteriaBuilder.conjunction();
    }

    //将约束条件信息添加到约束信息列表中
    public void add(Criterion criterion) {
        if (criterion != null)
            criterionList.add(criterion);
    }
}
