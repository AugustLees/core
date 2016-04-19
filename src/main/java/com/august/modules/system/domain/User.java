package com.august.modules.system.domain;

import com.august.common.utils.Collections3;
import com.august.common.utils.Global;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.modules.system.domain.hibernate
 * Author: August
 * Update: August(2015/9/9)
 * Description:定义用户信息类
 */
@Entity
@Table(name = "user")
public class User extends BaseDomain {

    /**
     * 归属公司
     */
    @JsonIgnore
    @Column(name = "company")
    @NotNull(message = "归属公司不能为空")
    private Office company;

    /**
     * 归属部门
     */

    @NotNull(message = "归属部门不能为空")
    @JsonIgnore
    @Column(name = "office")

    private Office office;

    /**
     * 登录名
     */
    @Column(name = "loginName")
    @Length(min = 1, max = 100, message = "登录名长度必须介于 1 和 100 之间")
    private String loginName;
    /**
     * 用户密码
     */
    @JsonIgnore
    @Column(name = "password", nullable = false)
    @Length(min = 1, max = 100, message = "密码长度必须介于 1 和 100 之间")
    private String password;

    /**
     * 工号
     */
    @Column(name = "employeeID")
    @Length(min = 1, max = 100, message = "工号长度必须介于 1 和 100 之间")
    private String employeeID;
    /**
     * 用户姓名
     */
    @Column(name = "name")
    @Length(min = 1, max = 100, message = "姓名长度必须介于 1 和 100 之间")
    private String name;

    /**
     * 用户邮箱
     */
    @Column(name = "email", nullable = false, unique = true)
    @Email(message = "邮箱格式不正确")
    @Length(min = 0, max = 200, message = "邮箱长度必须介于 1 和 200 之间")
    private String email;

    /**
     * 电话
     */
    @Column(name = "phone", nullable = false, unique = true)
    @Length(min = 0, max = 200, message = "电话长度必须介于 1 和 200 之间")
    private String phone;

    /**
     * 手机
     */
    @Column(name = "mobile", nullable = false, unique = true)
    @Length(min = 0, max = 200, message = "手机长度必须介于 1 和 200 之间")
    private String mobile;


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
    @Column(insertable = true, updatable = true, precision = 10, scale = 2, columnDefinition = "DECIMAL(12,2) DEFAULT 0.00")
    private Double salary;

    /**
     * 用户类型
     */
    @Column(name = "userType")
    @Length(min = 0, max = 100, message = "用户类型长度必须介于 1 和 100 之间")
    private String userType;

    /**
     * 最后登陆IP
     */
    @Column(name = "loginIp")
    private String loginIp;

    /**
     * 最后登陆日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:SS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:SS", timezone = "GMT+8")
    @Column(name = "loginDate")
    private Date loginDate;

    /**
     * 是否允许登陆
     */
    @Column(name = "loginFlag")
    private String loginFlag;

    /**
     * 头像
     */
    @Column(name = "photo")
    private String photo;

    /**
     * 原登录名
     */
    @Column(name = "oldLoginName")
    private String oldLoginName;

    /**
     * 新密码
     */
    @Column(name = "newPassword")
    private String newPassword;

    /**
     * 上次登陆IP
     */
    @Column(name = "oldLoginIp")
    private String oldLoginIp;

    /**
     * 上次登陆日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:SS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:SS", timezone = "GMT+8")
    @Column(name = "oldLoginDate")
    private Date oldLoginDate;

    /**
     * 根据角色查询用户条件
     */
    @Column(name = "role")
    private Role role;

    /**
     * 拥有角色列表
     */
    @Transient
    @JsonIgnore
    private List<Role> roleList = new ArrayList<Role>();

    /**
     * 创建用户构造器，并初始化登陆标识为允许登录
     */
    public User() {
        super();
        this.loginFlag = Global.YES;
    }

    /**
     * 创建ID为指定ID的用户构造器
     *
     * @param id 创建的用户ID
     */
    public User(Long id) {
        super(id);
        this.loginName = loginName;
    }

    /**
     * 创建某个角色的用户构造器
     *
     * @param role 角色信息
     */
    public User(Role role) {
        super();
        this.role = role;
    }

    public Office getCompany() {
        return company;
    }

    public void setCompany(Office company) {
        this.company = company;
    }

    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    public String getLoginFlag() {
        return loginFlag;
    }

    public void setLoginFlag(String loginFlag) {
        this.loginFlag = loginFlag;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getOldLoginName() {
        return oldLoginName;
    }

    public void setOldLoginName(String oldLoginName) {
        this.oldLoginName = oldLoginName;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getOldLoginIp() {
        if (oldLoginIp == null) return loginIp;
        return oldLoginIp;
    }

    public void setOldLoginIp(String oldLoginIp) {
        this.oldLoginIp = oldLoginIp;
    }

    public Date getOldLoginDate() {
        if (oldLoginDate == null) return loginDate;
        return oldLoginDate;
    }

    public void setOldLoginDate(Date oldLoginDate) {
        this.oldLoginDate = oldLoginDate;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    @JsonIgnore
    @Transient
    public List<Long> getRoleIdList() {
        List<Long> roleIdList = new ArrayList<Long>();
        for (Role role : roleList) {
            roleIdList.add(role.getId());
        }
        return roleIdList;
    }

    public void setRoleIdList(List<Long> roleIdList) {
        roleList = new ArrayList<Role>();
        for (Long roleID : roleIdList) {
            Role role = new Role();
            role.setId(roleID);
            roleList.add(role);
        }
    }

    /**
     * 用户拥有的角色名称字符串，多个角色名称用“,”分割
     * @return 返回角色名称字符串
     */
    @Transient
    public String getRoleNames(){
        return Collections3.extractToString(roleList,"name",",");
    }

    public boolean isAdmin(){
        return isAdmin(this.getId() );
    }

    /**
     *  判断是否是超级管理员
     * @param id
     * @return
     */
    private boolean isAdmin(Long id) {
        return id!=null&&"1".equals(id);
    }

}
