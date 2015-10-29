package com.august.domain;


import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.domain.hibernate
 * Author: August
 * Update: August(2015/10/27)
 * Description:创建用户角色实体
 */
@Entity
@Table(name = "role")
public class Role extends BaseDomain {
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "id")
    @Column(name = "office", nullable = false)
    private Office office;    // 归属机构

    @Column(name = "name", length = 100, nullable = false)
    private String name;    // 角色名称

    @Column(name = "en_name", length = 100, nullable = false)
    private String enName;    // 英文名称

    @Column(name = "role_type", length = 100, nullable = false)
    private String roleType;// 权限类型

    @Column(name = "data_scope")
    private String dataScope;// 数据范围

    @Column(name = "old_name")
    private String oldName;    // 原角色名称

    @Column(name = "old_en_name")
    private String oldEnName;    // 原英文名称

    @Column(name = "sysData")
    private String sysData;        //是否是系统数据

    @Column(name = "usable")
    private String usable;        //是否是可用
    @Transient
    private User user;        // 根据用户ID查询角色列表
    @Transient
    private List<Menu> menuList = new ArrayList<>(); // 拥有菜单列表
    @Transient
    private List<Office> officeList = new ArrayList<>(); // 按明细设置数据范围

    // 数据范围（1：所有数据；2：所在公司及以下数据；3：所在公司数据；4：所在部门及以下数据；5：所在部门数据；8：仅本人数据；9：按明细设置）
    public static final String DATA_SCOPE_ALL = "1";
    public static final String DATA_SCOPE_COMPANY_AND_CHILD = "2";
    public static final String DATA_SCOPE_COMPANY = "3";
    public static final String DATA_SCOPE_OFFICE_AND_CHILD = "4";
    public static final String DATA_SCOPE_OFFICE = "5";
    public static final String DATA_SCOPE_SELF = "8";
    public static final String DATA_SCOPE_CUSTOM = "9";

    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public String getDataScope() {
        return dataScope;
    }

    public void setDataScope(String dataScope) {
        this.dataScope = dataScope;
    }

    public String getOldName() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    public String getOldEnName() {
        return oldEnName;
    }

    public void setOldEnName(String oldEnName) {
        this.oldEnName = oldEnName;
    }

    public String getSysData() {
        return sysData;
    }

    public void setSysData(String sysData) {
        this.sysData = sysData;
    }

    public String getUsable() {
        return usable;
    }

    public void setUsable(String usable) {
        this.usable = usable;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Menu> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<Menu> menuList) {
        this.menuList = menuList;
    }

    public List<Office> getOfficeList() {
        return officeList;
    }

    public void setOfficeList(List<Office> officeList) {
        this.officeList = officeList;
    }

    public static String getDataScopeAll() {
        return DATA_SCOPE_ALL;
    }

    public static String getDataScopeCompanyAndChild() {
        return DATA_SCOPE_COMPANY_AND_CHILD;
    }

    public static String getDataScopeCompany() {
        return DATA_SCOPE_COMPANY;
    }

    public static String getDataScopeOfficeAndChild() {
        return DATA_SCOPE_OFFICE_AND_CHILD;
    }

    public static String getDataScopeOffice() {
        return DATA_SCOPE_OFFICE;
    }

    public static String getDataScopeSelf() {
        return DATA_SCOPE_SELF;
    }

    public static String getDataScopeCustom() {
        return DATA_SCOPE_CUSTOM;
    }

    /**
     * 获取菜单ID列表
     *
     * @return 返回menu菜单ID列表
     */
    @Transient
    public List<Long> getMenuIdList() {
        List<Long> menuIdList = new ArrayList<>();
        for (Menu menu : menuList) {
            menuIdList.add(menu.getId());
        }
        return menuIdList;
    }

    /**
     * 设置菜单ID列表
     *
     * @param menuIdList
     */
    public void setMenuIdList(List<Long> menuIdList) {
        menuList = new ArrayList<>();
        for (Long menuId : menuIdList) {
            Menu menu = new Menu();
            menu.setId(menuId);
            menuList.add(menu);
        }
    }
    @Transient
    public String getMenuIds() {
        return StringUtils.join(getMenuIdList(), ",");
    }

    public void setMenuIds(String menuIds) {
        menuList = new ArrayList<>();
        if (menuIds != null){
            Long[] ids = /*StringUtils.split(menuIds, ",").*/{};
//            setMenuIdList(new ArrayList<Long>().add(ids));
        }
    }


    @Transient
    public List<Long> getOfficeIdList() {
        List<Long> officeIdList = new ArrayList<>();
        for (Office office : officeList) {
            officeIdList.add(office.getId());
        }
        return officeIdList;
    }

    public void setOfficeIdList(List<Long> officeIdList) {
        officeList =  new ArrayList<>();
        for (Long officeId : officeIdList) {
            Office office = new Office();
            office.setId(officeId);
            officeList.add(office);
        }
    }
    @Transient
    public String getOfficeIds() {
        return StringUtils.join(getOfficeIdList(), ",");
    }

    public void setOfficeIds(String officeIds) {
        officeList = new ArrayList<>();
        if (officeIds != null){
            String[] ids = StringUtils.split(officeIds, ",");
//            setOfficeIdList(new ArrayList<>().toArray(ids));
        }
    }
    /**
     * 获取权限字符串列表
     */
    @Transient
    public List<String> getPermissions() {
        List<String> permissions = new ArrayList<>();
        for (Menu menu : menuList) {
            if (menu.getPermission()!=null && !"".equals(menu.getPermission())){
                permissions.add(menu.getPermission());
            }
        }
        return permissions;
    }
}
