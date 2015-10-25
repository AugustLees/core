package com.august.dao.repositories;

import com.august.domain.hibernate.JobSchedule;
import com.august.domain.hibernate.User;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.dao
 * Author: August
 * Update: August(2015/9/9)
 * Description:测试用户数据库操作层接口
 */
public interface UserRepository  extends BaseRepository<User, Integer> {
}
