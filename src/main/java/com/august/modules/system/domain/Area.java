package com.august.modules.system.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.modules.system.domain.hibernate
 * Author: August
 * Update: August(2015/10/28)
 * Description:创建用户区域类
 */
@Entity
@Table(name = "area")
public class Area extends TreeDomain<Area> {
    @Column(name = "name" ,length = 1,nullable = false)
    private String code;    // 区域编码
    @Column(name = "name" ,length = 100)
    private String type;    // 区域类型（1：国家；2：省份、直辖市；3：地市；4：区县）

    @JsonBackReference
    @Override
    public Area getParent() {
        return parent;
    }

    @Override
    public void setParent(Area parent) {
        this.parent = parent;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
