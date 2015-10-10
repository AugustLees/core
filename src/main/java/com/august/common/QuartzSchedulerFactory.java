package com.august.common;

import com.august.domain.hibernate.JobSchedule;
import com.august.utils.StaticConstant;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.utils
 * Author: August
 * Update: August(2015/9/27)
 * Description:调度任务执行处 无状态
 */
public class QuartzSchedulerFactory implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobSchedule jobSchedule = (JobSchedule) jobExecutionContext.getMergedJobDataMap().get(StaticConstant.SCHEDULE_JOB_NAME);
        JobScheduleUtils.invokeMethod(jobSchedule);
    }
}
