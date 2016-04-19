package com.august.modules.system.shiro;

import com.august.common.SpringUtils;
import com.august.common.SystemService;
import com.august.common.servlet.ValidateCodeServlet;
import com.august.common.utils.Encodes;
import com.august.common.utils.Global;
import com.august.common.utils.StringUtils;
import com.august.modules.system.controller.LoginController;
import com.august.modules.system.domain.Menu;
import com.august.modules.system.domain.Role;
import com.august.modules.system.domain.User;
import com.august.modules.system.utils.LogUtils;
import com.august.modules.system.utils.Servlets;
import com.august.modules.system.utils.UserUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.common
 * Author: August
 * Update: August(2016/4/8)
 * Description:系统安全认证实现类
 */
@Service
@Description("系统安全认证实现类")
public class SystemAuthorizingRealm extends AuthorizingRealm {
    //定义日志记录器
    private static final Logger LOGGER = LoggerFactory.getLogger(SystemAuthorizingRealm.class);
    //引入系统服务信息类
    private SystemService systemService;

    /**
     * 获取系统业务对象
     *
     * @return
     */
    public SystemService getSystemService() {
        //如果不存在该对象，则通过springUtils 从上下文中获取
        if (systemService == null) systemService = SpringUtils.getBean(SystemService.class);
        return systemService;
    }

    /**
     * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用
     *
     * @param principalCollection
     * @return 授权认证信息
     */
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //获取激活状态下的委托信息
        Principal principal = (Principal) getAvailablePrincipal(principalCollection);
        //获取当前已经登录的用户
        if (!Global.TRUE.equals(Global.getConfig("user.multiAccountLogin"))) {
            //获取当前状态下的激活活动的会话信息
            Collection<Session> sessions = getSystemService().getSessionDao().getActiveSessions(true, principal, UserUtil.getSession());
            if (sessions.size() > 0) {
                //如果是登录进来的，则踢出已在线用户
                if (UserUtil.getSubject().isAuthenticated()) {
                    for (Session session : sessions) {
                        getSystemService().getSessionDao().delete(session);
                    }
                } else {
                    //记住我进来的，并且当前用户已登录，则退出当前用户提示信息
                    UserUtil.getSubject().logout();
                    throw new AuthenticationException("msg:账号已在其他地方登录，请重新登录。");
                }
            }
        }
        User user = getSystemService().getUserByLoginName(principal.getLoginName());
        if (user != null) {
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            //获取用户的菜单列表
            List<Menu> menuList = UserUtil.getMenuList();
            for (Menu menu : menuList) {
                if (StringUtils.isNotBlank(menu.getPermission())) {
                    //添加基于permission的权限信息
                    for (String permission : StringUtils.split(menu.getPermission(), ","))
                        info.addStringPermission(permission);
                }
            }
            //添加用户权限
            info.addStringPermission("user");
            //添加用户角色信息
            for (Role role : user.getRoleList())
                info.addRole(role.getEnName());
            //更新本次登录IP和时间
            getSystemService().updateUserLoginInfo(user);
            //记录登录日志
            LogUtils.saveLog(Servlets.getRequest(), "系统登录");
            return info;
        }
        return null;
    }

    /**
     * 认证回调函数，登录时调用
     *
     * @param authenticationToken 授权令牌
     * @return 授权认证信息
     */
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) {
        //转换令牌认证信息为用户名密码令牌
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
        //获取当前激活状态的会话数
        int activeSessionSize = getSystemService().getSessionDao().getActiveSessions(false).size();
        //打印日志信息
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("login submit, active session size: {}, username: {}", activeSessionSize, usernamePasswordToken.getUsername());
        }
        //验证登录验证码
        if (LoginController.isValidateCodeLogin(usernamePasswordToken.getUsername(), false, false)) {
            //获取会话信息
            Session session = UserUtil.getSession();
            //从会话信息中获取到对应的验证码信息
            String code = (String) session.getAttribute(ValidateCodeServlet.VALIDATE_CODE);
            //如果验证码信息和缓存中记录的验证码信息不一致，则返回验证码错误信息
            if (usernamePasswordToken.getCaptcha() == null
                    || usernamePasswordToken.getCaptcha().toUpperCase().equals(code))
                throw new AuthenticationException("msg:验证码错误，请重试。");
        }

        //校验用户名密码，首先根据用户名获取对应的用户信息
        User user = getSystemService().getUserByLoginName(usernamePasswordToken.getUsername());
        //如果用户不为空，则需要根据用户状态或者密码进行校验
        if (user != null) {
            if (Global.FALSE.equals(user.getLoginFlag())) throw new AuthenticationException("msg:该账号禁止登录");
            //将你传入的密码进行HEX解码获取salt值
            byte[] salt = Encodes.decodeHex(user.getPassword().substring(0, 16));
            //进行简单的授权认证信息
            return new SimpleAuthenticationInfo(new Principal(user, usernamePasswordToken.isMobileLogin()),
                    user.getPassword().substring(16),
                    ByteSource.Util.bytes(salt), getName());
        }
        return null;
    }


    /**
     * 静态内部类，用于授权用户信息
     */
    public static class Principal  implements Serializable {
        private Long id;//编号
        private String loginName;//登录名
        private String name;//姓名
        private boolean mobileLogin;//是否手机登录

        public Principal(User user, boolean mobileLogin) {
            this.id = user.getId();
            this.loginName = user.getLoginName();
            this.name = user.getName();
            this.mobileLogin = mobileLogin;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isMobileLogin() {
            return mobileLogin;
        }

        public void setMobileLogin(boolean mobileLogin) {
            this.mobileLogin = mobileLogin;
        }

        public String getSessionID() {
            return (String) UserUtil.getSession().getId();
        }
    }
}































