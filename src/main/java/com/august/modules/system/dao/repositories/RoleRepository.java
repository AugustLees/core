package com.august.modules.system.dao.repositories;

import com.august.modules.system.domain.Role;

import java.util.List;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.modules.system.dao.repositories
 * Author: August
 * Update: August(2015/10/27)
 * Description:
 */
public interface RoleRepository extends BaseRepository<Role, Integer> {
    //根据role获取对应的角色列表
    List<Role> findList(Role role);
}
