package com.august.domain.hibernate;

import com.august.domain.BaseDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
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
    private Office office;	// 归属机构

    @Column(name = "name" ,length = 100)
    private String name; 	// 角色名称

    @Column(name = "en_name" ,length = 100)
    private String enName;	// 英文名称

    @Column(name = "role_type" ,length = 100)
    private String roleType;// 权限类型

    @Column(name = "data_scope" )
    private String dataScope;// 数据范围

    @Column(name = "old_name" )
    private String oldName; 	// 原角色名称

    @Column(name = "old_en_name" )
    private String oldEnName;	// 原英文名称

    @Column(name = "sysData" )
    private String sysData; 		//是否是系统数据

    @Column(name = "usable" )
    private String usable; 		//是否是可用

    private User user;		// 根据用户ID查询角色列表

    private List<Menu> menuList = Lists.newArrayList(); // 拥有菜单列表
    private List<Office> officeList = Lists.newArrayList(); // 按明细设置数据范围

    // 数据范围（1：所有数据；2：所在公司及以下数据；3：所在公司数据；4：所在部门及以下数据；5：所在部门数据；8：仅本人数据；9：按明细设置）
    public static final String DATA_SCOPE_ALL = "1";
    public static final String DATA_SCOPE_COMPANY_AND_CHILD = "2";
    public static final String DATA_SCOPE_COMPANY = "3";
    public static final String DATA_SCOPE_OFFICE_AND_CHILD = "4";
    public static final String DATA_SCOPE_OFFICE = "5";
    public static final String DATA_SCOPE_SELF = "8";
    public static final String DATA_SCOPE_CUSTOM = "9";
}
