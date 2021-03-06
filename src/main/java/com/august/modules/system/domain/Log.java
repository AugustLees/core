package com.august.modules.system.domain;

import com.august.common.utils.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.Map;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.modules.system.domain.hibernate
 * Author: August
 * Update: August(2015/10/28)
 * Description: 定义日志信息类
 */
@Entity
@Table(name = "log")
public class Log extends BaseDomain {
    // 日志类型（1：接入日志；2：错误日志）
    public static final String TYPE_ACCESS = "1";
    public static final String TYPE_EXCEPTION = "2";

    @Column(name = "type")
    private String type;            // 日志类型（1：接入日志；2：错误日志）

    @Column(name = "title")
    private String title;           // 日志标题

    @Column(name = "remote_address")
    private String remoteAddress;   // 操作用户的IP地址

    @Column(name = "request_uri")
    private String requestUri;      // 操作的URI

    @Column(name = "method")
    private String method;          // 操作的方式

    @Column(name = "params")
    private String params;          // 操作提交的数据

    @Column(name = "user_agent")
    private String userAgent;       // 操作用户代理信息

    @Column(name = "exception")
    private String exception;       // 异常信息

    @Column(name = "begin_date")
    private Date beginDate;        // 开始日期

    @Column(name = "end_date")
    private Date endDate;           // 结束日期


    /**
     * 设置请求参数
     *
     * @param paramMap
     */
    @Transient
    public void setParams(Map paramMap) {
        if (paramMap == null) {
            return;
        }
        StringBuilder params = new StringBuilder();
        for (Map.Entry<String, String[]> param : ((Map<String, String[]>) paramMap).entrySet()) {
            params.append(("".equals(params.toString()) ? "" : "&") + param.getKey() + "=");
            String paramValue = (param.getValue() != null && param.getValue().length > 0 ? param.getValue()[0] : "");
            params.append(StringUtils.abbr(StringUtils.endsWithIgnoreCase(param.getKey(), "password") ? "" : paramValue, 100));
        }
        this.params = params.toString();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * 执行插入方法之前，需要手动调用
     */
    public void preInsert() {
    }
}
