package com.august.modules.system.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.modules.system.domain
 * Author: August
 * Update: August(2015/10/28)
 * Description:创建基本树工具类
 */
// JPA基类标识
@MappedSuperclass
public abstract class TreeDomain<T> extends BaseDomain {
    protected String parentIds; // 所有父级编号
    @Column(name = "parent_ids",length = 2000)
    protected T parent;    // 父级编号
    @Column(name = "name",length = 100)
    protected String name;    // 机构名称
    @Column(name = "sort")
    protected Integer sort;        // 排序

    @JsonBackReference
    public abstract T getParent();

    /**
     * 父对象，只能通过子类实现，父类实现mybatis无法读取
     * @return
     */
    public abstract void setParent(T parent);

    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

}
