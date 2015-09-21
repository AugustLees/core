package com.august.dao.repositories;

import com.august.domain.hibernate.User;
import com.august.utils.JPATx;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnit;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.dao
 * Author: August
 * Update: August(2015/9/9)
 * Description:测试用户数据库操作层接口
 */
public interface UserRepository extends PagingAndSortingRepository<User, Integer> {
}
