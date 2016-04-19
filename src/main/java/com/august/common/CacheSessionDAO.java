package com.august.common;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.common
 * Author: August
 * Update: August(2016/4/8)
 * Description:系统安全认证实现类
 */
public class CacheSessionDAO extends EnterpriseCacheSessionDAO implements SessionDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheSessionDAO.class);

    @Override
    protected void doUpdate(Session session) {
        //如果没有session或者session的ID为空，则直接返回，不予修改
        if (session == null || session.getId() == null) return;
        //获取请求信息
        HttpServletRequest request=Servlets.getRequest();
        if(request!=null){
            String uri=request.getServletPath();
            if(se)
        }
        super.doUpdate(session);
    }
}
