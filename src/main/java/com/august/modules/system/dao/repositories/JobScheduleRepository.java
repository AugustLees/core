package com.august.modules.system.dao.repositories;

import com.august.modules.system.domain.JobSchedule;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.modules.system.dao.repositorie
 * Author: August
 * Update: August(2015/9/26)
 * Description:定义调度任务数据库层操作接口
 */
@Repository
public interface JobScheduleRepository extends BaseRepository<JobSchedule, Long> {
    @Modifying(clearAutomatically = true)
    @Query("UPDATE JobSchedule  SET cronExpression =:cron WHERE id =:id")
    void updateCron(@Param("id") Long id, @Param("cron") String cron);

    @Query("select job from JobSchedule job WHERE job.id =:id and job.cronExpression =:cron ")
    void select(@Param("id") Long id, @Param("cron") String cron);
}
