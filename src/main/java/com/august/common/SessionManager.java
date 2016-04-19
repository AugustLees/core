package com.august.common;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.common
 * Author: August
 * Update: August(2016/4/7)
 * Description:自定义WEB会话管理操作类
 */
public class SessionManager extends DefaultWebSessionManager {
    /**
     * 重写获取session的方法，返回序列化的sessionID
     * 当前逻辑为，如果参数中包含“_sessionID”参数，则使用获取到的该参数，并修正请求数据中的相应信息，否则还是使用原来的session
     *
     * @param request  请求信息
     * @param response 相应结果
     * @return 序列化的sessionID
     */
    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        //如果参数中包含“_sessionID”参数，则使用此sessionID会话。例如：http://IP/project?_sessionID=xxx&_cookie=true
        String sessionID = request.getParameter("_sessionID");
        if (StringUtils.isNotBlank(sessionID)) {
            //是否将sessionID保存到cookie，浏览器模式下使用此参数
            if (WebUtils.isTrue(request, "_cookie")) {
                //转换当前请求参数和相应结果为HTTP发送的请求及响应
                HttpServletRequest httpServletRequest = (HttpServletRequest) request;
                HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                //获取cookie信息
                Cookie template = getSessionIdCookie();
                //创建一个简单的cookie信息
                Cookie cookie = new SimpleCookie(template);
                //设定cookie值并保存到请求数据中
                cookie.setValue(sessionID);
                cookie.saveTo(httpServletRequest, httpServletResponse);
            }
            //获取当前session状态
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE,
                    ShiroHttpServletRequest.URL_SESSION_ID_SOURCE);//session来源与URL
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, sessionID);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
            return sessionID;
        } else
            return super.getSessionId(request, response);
    }

    @Override
    public void validateSessions() {
        super.validateSessions();
    }

    @Override
    protected Session retrieveSession(SessionKey sessionKey) {
        try {
            return super.retrieveSession(sessionKey);
        } catch (UnknownSessionException e) {
            // 获取不到SESSION不抛出异常
            return null;
        }
    }

    @Override
    public Date getStartTimestamp(SessionKey key) {
        try {
            return super.getStartTimestamp(key);
        } catch (InvalidSessionException e) {
            // 获取不到SESSION不抛出异常
            return null;
        }
    }

    @Override
    public Date getLastAccessTime(SessionKey key) {
        try {
            return super.getLastAccessTime(key);
        } catch (InvalidSessionException e) {
            // 获取不到SESSION不抛出异常
            return null;
        }
    }

    @Override
    protected long getTimeout(Session session) {
        try {
            return super.getTimeout(session);
        } catch (InvalidSessionException e) {
            // 获取不到SESSION不抛出异常
            return 0;
        }
    }

    @Override
    public void setTimeout(SessionKey key, long maxIdleTimeInMillis) {
        try {
            super.setTimeout(key, maxIdleTimeInMillis);
        } catch (InvalidSessionException e) {
            // 获取不到SESSION不抛出异常
        }
    }

    @Override
    public void touch(SessionKey key) {
        try {
            super.touch(key);
        } catch (InvalidSessionException e) {
            // 获取不到SESSION不抛出异常
        }
    }

    @Override
    public String getHost(SessionKey key) {
        try {
            return super.getHost(key);
        } catch (InvalidSessionException e) {
            // 获取不到SESSION不抛出异常
            return null;
        }
    }

    @Override
    public Collection<Object> getAttributeKeys(SessionKey key) {
        try {
            return super.getAttributeKeys(key);
        } catch (InvalidSessionException e) {
            // 获取不到SESSION不抛出异常
            return null;
        }
    }

    @Override
    public Object getAttribute(SessionKey sessionKey, Object attributeKey) {
        try {
            return super.getAttribute(sessionKey, attributeKey);
        } catch (InvalidSessionException e) {
            // 获取不到SESSION不抛出异常
            return null;
        }
    }

    @Override
    public void setAttribute(SessionKey sessionKey, Object attributeKey, Object value) {
        try {
            super.setAttribute(sessionKey, attributeKey, value);
        } catch (InvalidSessionException e) {
            // 获取不到SESSION不抛出异常
        }
    }

    @Override
    public Object removeAttribute(SessionKey sessionKey, Object attributeKey) {
        try {
            return super.removeAttribute(sessionKey, attributeKey);
        } catch (InvalidSessionException e) {
            // 获取不到SESSION不抛出异常
            return null;
        }
    }

    @Override
    public void stop(SessionKey key) {
        try {
            super.stop(key);
        } catch (InvalidSessionException e) {
            // 获取不到SESSION不抛出异常
        }
    }

    @Override
    public void checkValid(SessionKey key) {
        try {
            super.checkValid(key);
        } catch (InvalidSessionException e) {
            // 获取不到SESSION不抛出异常
        }
    }

    @Override
    protected Session doCreateSession(SessionContext context) {
        try {
            return super.doCreateSession(context);
        } catch (IllegalStateException e) {
            return null;
        }
    }

    /**
     * 重写新session实例获取方式，并设定全局session超时时间
     *
     * @param context
     * @return
     */
    @Override
    protected Session newSessionInstance(SessionContext context) {
        Session session = super.newSessionInstance(context);
        session.setTimeout(getGlobalSessionTimeout());
        return session;
    }

    /**
     * 重写启动方法，调用父类启动方法，如果出现空指针异常，则创建一个新的session并设置ID为0进行返回
     *
     * @param context session 上下文内容
     * @return session信息
     */
    @Override
    public Session start(SessionContext context) {
        try {
            return super.start(context);
        } catch (NullPointerException e) {
            SimpleSession session = new SimpleSession();
            session.setId(0);
            return session;
        }
    }
}
