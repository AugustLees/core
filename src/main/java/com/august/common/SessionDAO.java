package com.august.common;

import org.apache.shiro.session.Session;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.common
 * Author: August
 * Update: August(2016/4/8)
 * Description:定义会话DAO实例接口
 */
public interface SessionDAO extends org.apache.shiro.session.mgt.eis.SessionDAO {
    /**
     * 获取活动会话集合
     *
     * @param includeLeave 是否包含离线（最后访问时间大于3分钟视为离线会话）
     * @return 返回活动会话集合
     */
    public Collection<Session> getActiveSessions(boolean includeLeave);

    /**
     * 获取活动会话集合
     *
     * @param includeLeave  是否包含离线（最后访问时间大于3分钟视为离线）
     * @param principal     根据登录者对象获取活动会话
     * @param filterSession 不为空，则过滤掉（不包含）这个会话
     * @return 活动会话集合
     */
    public Collection<Session> getActiveSessions(boolean includeLeave, Object principal, Session filterSession);
}
































