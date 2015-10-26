package com.august.utils;

import com.august.common.SpringUtils;
import com.august.dao.repositories.MenuRepository;
import com.august.dao.repositories.RoleRepository;
import com.august.dao.repositories.UserRepository;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.utils
 * Author: August
 * Update: August(2015/10/27)
 * Description:创建用户工具类
 */
public class UserUtil {
    private static UserRepository userDao = SpringUtils.getBean(UserRepository.class);
    private static RoleRepository roleDao = SpringUtils.getBean(RoleRepository.class);
    private static MenuRepository menuDao = SpringUtils.getBean(MenuRepository.class);
    private static AreaRepository areaDao = SpringUtils.getBean(AreaRepository.class);
    private static OfficeRepository officeDao = SpringUtils.getBean(OfficeRepository.class);

    public static final String USER_CACHE = "userCache";
    public static final String USER_CACHE_ID_ = "id_";
    public static final String USER_CACHE_LOGIN_NAME_ = "ln";
    public static final String USER_CACHE_LIST_BY_OFFICE_ID_ = "oid_";

    public static final String CACHE_ROLE_LIST = "roleList";
    public static final String CACHE_MENU_LIST = "menuList";
    public static final String CACHE_AREA_LIST = "areaList";
    public static final String CACHE_OFFICE_LIST = "officeList";
    public static final String CACHE_OFFICE_ALL_LIST = "officeAllList";
}
