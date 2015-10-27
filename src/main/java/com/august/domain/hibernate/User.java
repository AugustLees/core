package com.august.domain.hibernate;

import com.august.domain.BaseDomain;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.domain.hibernate
 * Author: August
 * Update: August(2015/9/9)
 * Description:测试用户案例
 */
@Entity
@Table(name = "user")
public class User extends BaseDomain {

    /**
     * 用户姓名
     */
    @Column(name = "name")
    private String name;

    /**
     * 用户邮箱
     */
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * 用户密码
     */
    @Column(nullable = false)
    private String password;

    /**
     * 出生日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Column(name = "birthday", updatable = false)
    private Date birthday;

    /**
     * 薪资
     */
    @Column(insertable = true, updatable = true, columnDefinition = "DECIMAL(12,2) DEFAULT 0.00")
    private Double salary;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getPassword() {

        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }
}
