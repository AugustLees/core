package com.august.domain.hibernate;

import javax.persistence.*;
import java.util.Date;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.domain.hibernate
 * Author: August
 * Update: August(2015/9/25)
 * Description:用于定义基本的数据库操作基本信息
 */
// JPA基类标识
@MappedSuperclass
public class BaseDomain {
    /**
     * ID信息
     * 自增长字段
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, length = 20)
    private Long id;

    /**
     * 创建日期
     */
    @Column(name = "create_time", nullable = false, insertable = true, updatable = false)
    private Date createTime;


    /**
     * 修改日期
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 删除日期
     */
    @Column(name = "delete_time")
    private Date deleteTime;

    /**
     * 创建人信息
     */
    @Column(name = "creator", nullable = false, insertable = true, updatable = false)
    private String creator;

    /**
     * 修改人信息
     */
    @Column(name = "creator")
    private String updater;

    /**
     * 删除人信息
     */
    @Column(name = "deleter")
    private String deleter;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public String getDeleter() {
        return deleter;
    }

    public void setDeleter(String deleter) {
        this.deleter = deleter;
    }
}
