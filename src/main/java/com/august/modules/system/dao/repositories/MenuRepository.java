package com.august.modules.system.dao.repositories;

import com.august.modules.system.domain.Menu;

import java.util.List;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.modules.system.dao.repositories
 * Author: August
 * Update: August(2015/10/27)
 * Description:用户菜单数据库操作类
 */
public interface MenuRepository extends BaseRepository<Menu, Long> {
    //根据用户信息获取对应的授权菜单明细
    List<Menu> findByUserId(Menu menu);
}
