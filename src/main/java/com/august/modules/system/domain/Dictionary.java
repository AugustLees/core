package com.august.modules.system.domain;

import com.august.modules.system.domain.BaseDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.modules.system.domain.hibernate
 * Author: August
 * Update: August(2015/10/28)
 * Description:定义数据字典类
 */
@Entity
@Table(name = "dictionary")
public class Dictionary extends BaseDomain {
    @Column(name = "value", length = 100, nullable = false)
    private String value;    // 数据值

    @Column(name = "label", length = 100, nullable = false)
    private String label;    // 标签名

    @Column(name = "type", length = 100, nullable = false)
    private String type;    // 类型

    @Column(name = "description", length = 100)
    private String description;// 描述

    @Column(name = "sort", nullable = false)
    private Integer sort;    // 排序

    @Column(name = "parentId", length = 100, nullable = false)
    private String parentId;//父Id

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
