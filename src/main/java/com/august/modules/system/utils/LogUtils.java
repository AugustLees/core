package com.august.modules.system.utils;

import com.august.common.SpringUtils;
import com.august.common.utils.CacheUtils;
import com.august.common.utils.Exceptions;
import com.august.common.utils.Global;
import com.august.common.utils.StringUtils;
import com.august.modules.system.dao.repositories.LogRepository;
import com.august.modules.system.dao.repositories.MenuRepository;
import com.august.modules.system.domain.Log;
import com.august.modules.system.domain.Menu;
import com.august.modules.system.domain.User;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.MediaTypeExpression;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.modules.system.utils
 * Author: August
 * Update: August(2016/4/19)
 * Description:日志记录工具类
 */
public class LogUtils {
    //定义缓存菜单节点路径集合
    public static final String CACHE_MENU_NAME_PATH_MAP = "menuNamePathMap";
    private static LogRepository logDao = SpringUtils.getBean(LogRepository.class);
    private static MenuRepository menuDao = SpringUtils.getBean(MenuRepository.class);

    /**
     * 保存日志信息
     *
     * @param request 请求信息
     * @param title   保存标题
     */
    public static void saveLog(HttpServletRequest request, String title) {
        saveLog(request, null, null, title);
    }

    /**
     * 保存日志信息
     *
     * @param request   请求信息
     * @param handler   操作
     * @param exception 可能出现的异常
     * @param title     标题
     */
    private static void saveLog(HttpServletRequest request, Object handler, Exception exception, String title) {
        //获取当前用户的信息
        User user = UserUtil.getUser();
        if (user != null && user.getId() != 0L) {
            Log log = new Log();
            log.setTitle(title);
            log.setTitle(exception == null ? Log.TYPE_ACCESS : Log.TYPE_EXCEPTION);
            log.setRemoteAddress(StringUtils.getRemoteAddress(request));
            log.setUserAgent(request.getHeader("user-agent"));
            log.setRequestUri(request.getRequestURI());
            log.setParams(request.getParameterMap());
            log.setMethod(request.getMethod());
            //异步保存日志
            new SaveLogThread(log, handler, exception).start();
        }
    }

    public static class SaveLogThread extends Thread {
        private Log log;
        private Object handler;
        private Exception exception;

        public SaveLogThread(Log log, Object handler, Exception exception) {
            super(SaveLogThread.class.getSimpleName());
            this.exception = exception;
            this.log = log;
            this.handler = handler;
        }

        @Override
        public void run() {
            //获取日志标题
            if (StringUtils.isBlank(log.getTitle())) {
                String permission = "";
                if (handler instanceof HandlerMethod) {
                    //获取调用方法信息
                    Method method = ((HandlerMethod) handler).getMethod();
                    RequiresPermissions requiresPermissions = method.getAnnotation(RequiresPermissions.class);
                    permission = (requiresPermissions != null ? StringUtils.join(requiresPermissions.value(), ",") : "");
                }
                log.setTitle(getMenuNamePath(log.getRequestUri(), permission));
            }
            //如果有异常，设置异常信息
            log.setException(Exceptions.getStackTraceAsString(exception));
            //如果无标题且无异常日志，则不保存信息
            if (StringUtils.isBlank(log.getTitle()) && StringUtils.isBlank(log.getException())) return;

            //保存日志信息
            log.preInsert();
            logDao.save(log);
        }

    }
    /**
     * 获取菜单名称路径（如：系统设置-机构用户-用户管理-编辑）
     *
     * @param requestUri 请求路径
     * @param permission 授权信息
     * @return
     */
    private static String getMenuNamePath(String requestUri, String permission) {
        String href = StringUtils.substringAfter(requestUri, Global.getAdminPath());
        Map<String, String> menuMap = (Map<String, String>) CacheUtils.get(CACHE_MENU_NAME_PATH_MAP);
        if (menuMap == null) {
            menuMap = new HashMap<String, String>();
            List<Menu> menuList = menuDao.findAll();
            for (Menu menu : menuList) {
                //遍历菜单信息获取菜单名称（如：系统设置-机构用户-用户管理-编辑）
                String namePath = "";
                if (menu.getParentIds() != null) {
                    List<String> namePathList = new ArrayList<String>();
                    for (String id : StringUtils.split(menu.getParentIds(), ",")) {
                        if (Menu.getRootId().equals(id)) continue;//过滤根节点
                        for (Menu menu1 : menuList) {
                            if (menu1.getId() == Long.valueOf(id).longValue()) {
                                namePathList.add(menu1.getName());
                                break;
                            }
                        }
                    }
                    namePathList.add(menu.getName());
                    namePath = StringUtils.join(namePathList, "-");
                }
                if (StringUtils.isNotBlank(menu.getHref()))
                    menuMap.put(menu.getHref(), namePath);
                else if (StringUtils.isNotBlank(menu.getPermission()))
                    for (String p : StringUtils.split(menu.getParentIds()))
                        menuMap.put(p, namePath);
            }
            CacheUtils.put(CACHE_MENU_NAME_PATH_MAP, menuMap);
        }
        String menuNamePath = menuMap.get(href);
        if (menuNamePath == null) {
            for (String p : StringUtils.split(permission)) {
                menuNamePath = menuMap.get(p);
                if (StringUtils.isNotBlank(menuNamePath)) break;
            }
            if (menuNamePath == null) return "";
        }
        return menuNamePath;
    }
}





































