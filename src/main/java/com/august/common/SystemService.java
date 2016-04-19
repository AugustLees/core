package com.august.common;

import com.august.modules.system.domain.User;
import com.august.modules.system.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.common
 * Author: August
 * Update: August(2016/4/8)
 * Description:系统管理，安全相关实体的管理类,包括用户、角色、菜单.
 */
@Service
@Transactional(readOnly = true)
@Description("系统管理，安全相关实体的管理类,包括用户、角色、菜单.")
public class SystemService {
    //定义HASH演算方式
    public static final String HASH_ALGORITHM = "SHA-1";

    //定义演算次数
    public static final int hash_interaction = 1024;

    //定义yan盐的长度
    public static final int SALT_SIZE = 8;

    @Autowired
    private SessionDAO sessionDao;

    /**
     * 获取sessionDAO
     *
     * @return sessionDao
     */
    public SessionDAO getSessionDao() {
        return sessionDao;
    }

    /**
     * 根据用户名获取对应的用户信息
     *
     * @param username 用户名称
     * @return 用户信息
     */
    public User getUserByLoginName(String username) {
        return UserUtil.getByLoginName(username);
    }

    /**
     * 根据用户ID获取对应的用户信息
     *
     * @param userID 用户信息ID
     * @return 对应的用户信息
     */
    public User getUser(String userID) {
        return UserUtil.get(userID);
    }


    /**
     * 更新用户登录信息
     *
     * @param user 用户信息
     */
    public void updateUserLoginInfo(User user) {

    }
}














