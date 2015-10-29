package com.august.domain;

import com.august.domain.BaseDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.domain.hibernate
 * Author: August
 * Update: August(2015/9/24)
 * Description:任务调度信息管理类
 * 用于定义任务调度信息
 */
@Entity
@Table(name = "jobSchedule", uniqueConstraints = {@UniqueConstraint(columnNames = {"job_name", "job_group"})})
public class JobSchedule extends BaseDomain {
    //可运行状态
    public static final String STATUS_RUNNING = "1";
    //非运行状态
    public static final String STATUS_NOT_RUNNING = "0";

    public static final String CONCURRENT_IS = "1";

    public static final String CONCURRENT_NOT = "0";

    /**
     * 任务名称
     */
    @Column(name = "job_name", nullable = false)
    private String jobName;

    /**
     * 任务分组
     */
    @Column(name = "job_group")
    private String jobGroup;

    /**
     * 任务状态 是否启动任务
     */
    @Column(name = "job_status")
    private String jobStatus;

    /**
     * 任务cron表达式
     */
    @Column(name = "cron_expression", nullable = false)
    private String cronExpression;

    /**
     * 任务描述信息
     */
    @Column(name = "description")
    private String description;

    /**
     * 任务是否有状态
     */
    @Column(name = "is_concurrent")
    private String isConcurrent;

    /**
     * spring的bean
     */
    @Column(name = "spring_id")
    private String springId;

    /**
     * 任务执行时调用哪个类的方法，
     * 此处的写法是包名+类名
     */
    @Column(name = "bean_class", nullable = false)
    private String beanClass;

    /**
     * 任务调用的方法名
     */
    @Column(name = "method_name", nullable = false)
    private String methodName;


    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(String beanClass) {
        this.beanClass = beanClass;
    }

    public String getIsConcurrent() {
        return isConcurrent;
    }

    public void setIsConcurrent(String isConcurrent) {
        this.isConcurrent = isConcurrent;
    }

    public String getSpringId() {
        return springId;
    }

    public void setSpringId(String springId) {
        this.springId = springId;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}
