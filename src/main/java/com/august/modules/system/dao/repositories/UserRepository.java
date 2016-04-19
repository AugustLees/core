package com.august.modules.system.dao.repositories;

import com.august.modules.system.domain.User;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.modules.system.dao
 * Author: August
 * Update: August(2015/9/9)
 * Description:测试用户数据库操作层接口
 */
public interface UserRepository  extends BaseRepository<User, Integer> {
    /**
     * 根据用户登录名获取用户信息
     * @param loginName 用户登录名
     * @return 用户信息
     */
    User getByLoginName(String loginName);
}
