package com.august.modules.system.utils;

import com.august.common.SpringUtils;
import com.august.common.utils.CacheUtils;
import com.august.modules.system.dao.repositories.MenuRepository;
import com.august.modules.system.dao.repositories.RoleRepository;
import com.august.modules.system.dao.repositories.UserRepository;
import com.august.modules.system.domain.Menu;
import com.august.modules.system.domain.Role;
import com.august.modules.system.domain.User;
import com.august.modules.system.shiro.SystemAuthorizingRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import java.util.List;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.modules.system.utils
 * Author: August
 * Update: August(2015/10/27)
 * Description:创建用户工具类
 */
public class UserUtil {
    private static UserRepository userDao = SpringUtils.getBean(UserRepository.class);
    private static RoleRepository roleDao = SpringUtils.getBean(RoleRepository.class);
    private static MenuRepository menuDao = SpringUtils.getBean(MenuRepository.class);
//    private static AreaRepository areaDao = SpringUtils.getBean(AreaRepository.class);
//    private static OfficeRepository officeDao = SpringUtils.getBean(OfficeRepository.class);

    public static final String USER_CACHE = "userCache";
    public static final String USER_CACHE_ID_ = "id_";
    public static final String USER_CACHE_LOGIN_NAME_ = "ln";
    public static final String USER_CACHE_LIST_BY_OFFICE_ID_ = "oid_";

    public static final String CACHE_ROLE_LIST = "roleList";
    public static final String CACHE_MENU_LIST = "menuList";
    public static final String CACHE_AREA_LIST = "areaList";
    public static final String CACHE_OFFICE_LIST = "officeList";
    public static final String CACHE_OFFICE_ALL_LIST = "officeAllList";

    /**
     * 根据ID获取用户信息
     *
     * @param id ID信息
     * @return 返回查询到的用户，如果查询不到，则返回null
     */
    public static User get(String id) {
        //从缓存中获取用户信息
        User user = (User) CacheUtils.get(USER_CACHE, USER_CACHE_ID_ + id);
        if (user == null) {
            user = userDao.getOne(Integer.valueOf(id));
            if (user == null) return null;
            //设置该用户角色信息
            user.setRoleList(roleDao.findList(new Role(user)));
            //将该用户信息写入缓存中，主键为用户ID，值为用户信息
            CacheUtils.put(USER_CACHE, USER_CACHE_ID_ + user.getId(), user);
            //将该用户信息写入缓存中，主键为用户登录名，值为用户信息
            CacheUtils.put(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getLoginName(), user);
        }
        return user;
    }

    /**
     * 根据登录名称获取用户
     *
     * @param loginName 登录名
     * @return 用户信息
     */
    public static User getByLoginName(String loginName) {
        //首先从缓存中获取用户信息
        User user = (User) CacheUtils.get(USER_CACHE, USER_CACHE_LOGIN_NAME_ + loginName);
        if (user == null) {
            //如果用户不存在，则直接从数据库中查询
            user = userDao.getByLoginName(loginName);
            if (user == null) return null;
            //设置该用户角色信息
            user.setRoleList(roleDao.findList(new Role(user)));
            //将该用户信息写入缓存中，主键为用户ID，值为用户信息
            CacheUtils.put(USER_CACHE, USER_CACHE_ID_ + user.getId(), user);
            //将该用户信息写入缓存中，主键为用户登录名，值为用户信息
            CacheUtils.put(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getLoginName(), user);
        }
        return user;
    }

    /**
     * 清除当前用户缓存信息
     */
    public static void clearCache() {
        //清除当前用户角色列表
        removeCache(CACHE_ROLE_LIST);
        //清除当前用户菜单列表
        removeCache(CACHE_MENU_LIST);
        //清除当前用户区域列表
        removeCache(CACHE_AREA_LIST);
        //清除当前用户组织机构列表
        removeCache(CACHE_OFFICE_LIST);
        //清除当前用户所有组织机构列表
        removeCache(CACHE_OFFICE_ALL_LIST);
        UserUtil.clearCache(getUser());
    }

    /**
     * 删除指定的key对应的缓存信息
     *
     * @param key 需要删除缓存的主键
     */
    private static void removeCache(String key) {
        getSession().removeAttribute(key);
    }

    /**
     * 清除指定用户缓存信息
     *
     * @param user 需要删除缓存信息的用户
     */
    private static void clearCache(User user) {
        //清除用户缓存节点下的ID为主键的缓存信息
        CacheUtils.remove(USER_CACHE, USER_CACHE_ID_ + user.getId());
        //清除用户缓存节点下的登录名为主键的缓存信息
        CacheUtils.remove(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getLoginName());
        //清除用户缓存节点下的登录名为主键的缓存信息
        CacheUtils.remove(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getOldLoginName());
        //如果该用户存在组织机构且组织机构的ID不为空，则删除该用户对应的组织结构ID的缓存信息
        if (user.getOffice() != null && user.getOffice().getId() != 0L)
            CacheUtils.remove(USER_CACHE, USER_CACHE_LIST_BY_OFFICE_ID_ + user.getOffice().getId());
    }

    /**
     * 获取用户信息
     */
    public static User getUser() {
        //获取用户的授权信息
        SystemAuthorizingRealm.Principal principal = getPrincipal();
        if (principal != null) {
            User user = get(principal.getName());
            if (user != null) return user;
            return new User();
        }
        //如果没有登录，则返回实例化空User对象
        return new User();
    }

    /**
     * 获取当前登录者对象
     *
     * @return 返回当前登录者对象
     */
    public static SystemAuthorizingRealm.Principal getPrincipal() {
        //获取当前登录者主体
        Subject subject = SecurityUtils.getSubject();
        //获取登录者主体对象
        SystemAuthorizingRealm.Principal principal = (SystemAuthorizingRealm.Principal) subject.getPrincipal();
        if (principal != null) return principal;
        return null;
    }

    /**
     * 获取会话信息
     *
     * @return 返回获取到的会话信息
     */
    public static Session getSession() {
        //获取会话认证主体
        Subject subject = SecurityUtils.getSubject();
        //获取会话信息
        Session session = subject.getSession(false);
        if (session == null) session = subject.getSession();
        if (session != null) return session;
        return null;
    }

    /**
     * 获取认证主体
     *
     * @return 返回获取到的认证主体
     */
    public static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    /**
     * 获取当前用户授权菜单列表
     *
     * @return 返回获取到的当前用户授权菜单列表
     */
    public static List<Menu> getMenuList() {
        //从缓存中获取当前登录用户的授权菜单列表
        List<Menu> menuList = (List<Menu>) getCache(CACHE_MENU_LIST);
        //如果没有缓存则去数据库中查询
        if (menuList == null) {
            //获取当前用户信息
            User user = getUser();
            //如果该用户是超级管理员，则授权所有菜单
            if (user.isAdmin())
                menuList = menuDao.findAll();
            else {
                //否则查询该用户的菜单
                Menu menu = new Menu();
                menu.setUserId(user.getId());
                menuList = menuDao.findByUserId(menu);
            }
            //将用户的授权菜单信息放入缓存中
            putCache(CACHE_MENU_LIST, menuList);
        }
        return menuList;
    }

    /**
     * 放入缓存信息
     *
     * @param cacheKey 缓存的主键
     * @param object   放入的对象信息
     */
    public static void putCache(String cacheKey, Object object) {
        getSession().setAttribute(cacheKey, object);
    }


    /**
     * 根据缓存主键查找对应的缓存信息
     *
     * @param cacheKey 缓存主键
     * @return 对应的缓存信息
     */
    public static Object getCache(String cacheKey) {
        return getCache(cacheKey, null);
    }

    /**
     * 根据缓存主键查找对应的缓存信息
     *
     * @param cacheKey     缓存主键
     * @param defaultValue 找不到对应的缓存信息时使用的默认值
     * @return
     */
    public static Object getCache(String cacheKey, Object defaultValue) {
        Object object = getSession().getAttribute(cacheKey);
        return object == null ? defaultValue : object;
    }
}






































