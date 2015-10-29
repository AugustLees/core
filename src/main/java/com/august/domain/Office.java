package com.august.domain;

import javax.persistence.*;
import java.util.List;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.domain.hibernate
 * Author: August
 * Update: August(2015/10/28)
 * Description:创建用户组织机构类
 */
@Entity
@Table(name = "office")
public class Office extends TreeDomain<Office> {
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "id")
    @Column(name = "area", nullable = false)
    private Area area;        // 归属区域

    @Column(name = "code", length = 100)
    private String code;    // 机构编码

    @Column(name = "type", length = 1)
    private String type;    // 机构类型（1：公司；2：部门；3：小组）

    @Column(name = "grade", length = 1)
    private String grade;    // 机构等级（1：一级；2：二级；3：三级；4：四级）

    @Column(name = "address")
    private String address; // 联系地址

    @Column(name = "zipCode", length = 100)
    private String zipCode; // 邮政编码

    @Column(name = "master", length = 100)
    private String master;    // 负责人

    @Column(name = "phone", length = 100)
    private String phone;    // 电话

    @Column(name = "fax", length = 200)
    private String fax;    // 传真

    @Column(name = "email", length = 200)
    private String email;    // 邮箱

    @Column(name = "usable")
    private boolean usable;//是否可用

    @Column(name = "primaryPerson", length = 200)
    private User primaryPerson;//主负责人

    @Column(name = "deputyPerson", length = 200)
    private User deputyPerson;//副负责人

    @Transient
    private List<String> childDeptList;//快速添加子部门


    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
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

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isUsable() {
        return usable;
    }

    public void setUsable(boolean usable) {
        this.usable = usable;
    }

    public User getPrimaryPerson() {
        return primaryPerson;
    }

    public void setPrimaryPerson(User primaryPerson) {
        this.primaryPerson = primaryPerson;
    }

    public User getDeputyPerson() {
        return deputyPerson;
    }

    public void setDeputyPerson(User deputyPerson) {
        this.deputyPerson = deputyPerson;
    }

    public List<String> getChildDeptList() {
        return childDeptList;
    }

    public void setChildDeptList(List<String> childDeptList) {
        this.childDeptList = childDeptList;
    }

    @Override
    public Office getParent() {
        return parent;
    }

    @Override
    public void setParent(Office parent) {
        this.parent = parent;
    }
}
