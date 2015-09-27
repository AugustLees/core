package com.august.domain.hibernate;

import org.hibernate.dialect.Ingres10Dialect;

import javax.persistence.*;
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

    private String name;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    private Date birthday;

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
}
