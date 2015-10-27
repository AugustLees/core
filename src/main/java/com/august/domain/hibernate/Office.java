package com.august.domain.hibernate;

import com.august.domain.BaseDomain;

import javax.persistence.Entity;
import javax.persistence.Table;
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
public class Office extends BaseDomain {
    private Area area;		// 归属区域
    private String code; 	// 机构编码
    private String type; 	// 机构类型（1：公司；2：部门；3：小组）
    private String grade; 	// 机构等级（1：一级；2：二级；3：三级；4：四级）
    private String address; // 联系地址
    private String zipCode; // 邮政编码
    private String master; 	// 负责人
    private String phone; 	// 电话
    private String fax; 	// 传真
    private String email; 	// 邮箱
    private String useable;//是否可用
    private User primaryPerson;//主负责人
    private User deputyPerson;//副负责人
    private List<String> childDeptList;//快速添加子部门


}
