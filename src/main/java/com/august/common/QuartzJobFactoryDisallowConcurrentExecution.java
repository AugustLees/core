package com.august.common;

import com.august.domain.JobSchedule;
import com.august.utils.StaticConstant;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.utils
 * Author: August
 * Update: August(2015/9/27)
 * Description:若一个方法一次执行不完下次轮转时则等待该方法执行完后才执行下一次操作
 */
@DisallowConcurrentExecution
public class QuartzJobFactoryDisallowConcurrentExecution implements Job {
    public static final Logger LOGGER = LoggerFactory.getLogger(QuartzJobFactoryDisallowConcurrentExecution.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobSchedule scheduleJob = (JobSchedule) context.getMergedJobDataMap().get(StaticConstant.SCHEDULE_JOB_NAME);
        JobScheduleUtils.invokeMethod(scheduleJob);
    }
}
