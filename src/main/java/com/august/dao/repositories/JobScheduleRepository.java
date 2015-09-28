package com.august.dao.repositories;

import com.august.domain.hibernate.JobSchedule;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.dao.repositorie
 * Author: August
 * Update: August(2015/9/26)
 * Description:定义调度任务数据库层操作接口
 */
public interface JobScheduleRepository extends PagingAndSortingRepository<JobSchedule, Long> {

    @Modifying(clearAutomatically = true)
    @Query("update JobSchedule  set cronExpression =:cron where id =:id")
    void updateCron(@Param("id") Long id, @Param("cron") String cron);
}
