package com.august.domain.hibernate;

import com.august.domain.BaseDomain;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import java.util.List;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.domain.hibernate
 * Author: August
 * Update: August(2015/10/27)
 * Description:创建用户菜单类
 */
public class Menu extends BaseDomain {
    @JsonBackReference//避免无限递归
    @Column(name = "parent", nullable = false)
    private Menu parent;                        // 父级菜单

    @Column(name = "parentIds", length = 2000)
    private String parentIds;                   // 所有父级编号

    @Column(name = "parentIds", length = 100)
    private String name;                        // 名称

    @Column(name = "href", length = 2000)
    private String href;                        // 链接

    @Column(name = "target", length = 20)
    private String target;                      // 目标（ mainFrame、_blank、_self、_parent、_top）

    @Column(name = "icon", length = 100)
    private String icon;                        // 图标

    @Column(name = "sort", nullable = false)
    private Integer sort;                       // 排序

    @Column(name = "isShow", length = 1)
    private boolean isShow;                     // 是否在菜单中显示（1：显示；0：不显示）

    @Column(name = "permission", length = 200)
    private String permission;                  // 权限标识

    @Column(name = "userId")
    private String userId;                      //用户ID


    public Menu getParent() {
        return parent;
    }

    public void setParent(Menu parent) {
        this.parent = parent;
    }

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

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setIsShow(boolean isShow) {
        this.isShow = isShow;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getParentId() {
        return parent != null && parent.getId() != null ? parent.getId().toString() : "0";
    }

    @JsonIgnore
    public static String getRootId() {
        return "1";
    }

    /**
     * 排序列表
     *
     * @param list       拍完顺序的列表
     * @param sourceList 需要排序的源列表
     * @param parentId   父节点ID
     * @param cascade    是否还有子节点
     */
    @JsonIgnore
    public static void sortList(List<Menu> list, List<Menu> sourceList, String parentId, boolean cascade) {
        /**
         * 遍历源列表进行排序
         */
        for (int i = 0; i < sourceList.size(); i++) {
            Menu menu = sourceList.get(i);
            if (menu.getParent() != null && menu.getParent().getId() != null
                    && menu.getParent().getId().equals(parentId)) {
                list.add(menu);
                if (cascade) {
                    // 判断是否还有子节点, 有则继续获取子节点
                    for (int j = 0; j < sourceList.size(); j++) {
                        Menu child = sourceList.get(j);
                        if (child.getParent() != null && child.getParent().getId() != null
                                && child.getParent().getId().equals(menu.getId())) {
                            sortList(list, sourceList, menu.getId().toString(), true);
                            break;
                        }
                    }
                }
            }
        }
    }
}
