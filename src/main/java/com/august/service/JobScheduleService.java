package com.august.service;

import com.august.dao.repositories.JobScheduleRepository;
import com.august.domain.hibernate.JobSchedule;
import com.august.utils.JPATx;
import com.august.utils.QuartzJobFactoryDisallowConcurrentExecution;
import com.august.utils.QuartzSchedulerFactory;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.service
 * Author: August
 * Update: August(2015/9/24)
 * Description:任务调度服务类
 * 用于调度任务信息的具体实现
 */
@Service
public class JobScheduleService {
    //定义任务管理器
    private static final Logger LOGGER = LoggerFactory.getLogger(JobScheduleService.class);
    //引入任务调度数据库操作层
    @Autowired
    private JobScheduleRepository jobScheduleRepository;

    //引入任务调度器工厂实例
    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    /**
     * 获取所有的调度任务信息列表
     *
     * @param jobSchedule 需要查询的参数集合
     * @return 查询到的任务信息列表
     */
    public List<JobSchedule> getJobScheduleList(JobSchedule jobSchedule) {
        return (List<JobSchedule>) jobScheduleRepository.findAll();
    }

    /**
     * 添加调度任务信息到数据库中进行保存
     *
     * @param jobSchedule 需要添加的调度任务信息
     */
    @JPATx
    public void addJobSchedule(JobSchedule jobSchedule) {
        jobScheduleRepository.save(jobSchedule);
    }

    /**
     * 根据给定ID信息进行相应的CRON表达式的修改
     *
     * @param id   需要修改CRON表达式的数据ID
     * @param cron 修改后的表达式信息
     */
    @JPATx
    public void updateCron(Long id, String cron) throws SchedulerException {
        //获取相应的ID记录信息
        JobSchedule jobSchedule = getJobScheduleById(id);
        if (jobSchedule == null) {
            return;
        }
        //更新调度信息表达式
        jobSchedule.setCronExpression(cron);
        //如果当前任务信息为在执行状态，
        if (JobSchedule.STATUS_RUNNING.equals(jobSchedule.getJobStatus())) {
            //修改执行中任务信息的执行时间表达式
            updateRunningCron(jobSchedule);
        }
//            jobScheduleRepository.

    }

    /**
     * 修改正在执行的任务执行时间表达式
     *
     * @param jobSchedule 需要修改的在任务调度器中存在的调度任务的信息
     */
    public void updateRunningCron(JobSchedule jobSchedule) throws SchedulerException {
        //获取任务调度器
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        //根据调度任务名称进行触发器的主键的获取
        TriggerKey triggerKey = TriggerKey.triggerKey(jobSchedule.getJobName());
        //根据触发器主键获取任务表达式信息
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        //创建一个新的定时任务时间表达式信息
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder
                .cronSchedule(jobSchedule.getCronExpression());
        //根据创建的时间表达式信息修改原来的主键为triggerKey的
        trigger = trigger.getTriggerBuilder()
                .withIdentity(triggerKey)
                .withSchedule(cronScheduleBuilder).build();
        //将新的表达式信息更新到调度器当中
        scheduler.rescheduleJob(triggerKey, trigger);
    }

    /**
     * 根据给定的ID进行相应任务调度信息的查找
     *
     * @param id 需要查找的信息记录的ID
     * @return 查找到的信息记录
     */
    public JobSchedule getJobScheduleById(Long id) {
        return jobScheduleRepository.findOne(id);
    }

    /**
     * 更改任务调度状态
     *
     * @param id  需要更改的任务调度信息的ID
     * @param cmd 需要进行更改的命令  start:启动任务；stop停止任务
     */
    public void changeJobScheduleStatus(long id, String cmd) throws SchedulerException {
        //获取需要进行修改的任务调度信息
        JobSchedule jobSchedule = getJobScheduleById(id);
        if (jobSchedule == null) {
            return;
        }
        if ("stop".equals(cmd)) {
            //如果是停止任务，则需要从调度管理中删除调度任务信息后然后再修改调度任务信息
            deleteRunningJobSchedule(jobSchedule);
            jobSchedule.setJobStatus(JobSchedule.STATUS_NOT_RUNNING);
        } else if ("start".equals(cmd)) {
            //如果是启动任务，则需要启动运行中的任务调度信息
            jobSchedule.setJobStatus(JobSchedule.STATUS_RUNNING);
            addRunningJobSchedule(jobSchedule);
        }

    }

    /**
     * 添加任务调度信息到任务调度器
     *
     * @param jobSchedule 需要添加到任务调度器的任务调度信息
     */
    public void addRunningJobSchedule(JobSchedule jobSchedule) throws SchedulerException {
        //保证该任务调度信息不为空，且必须是可运行状态
        if (jobSchedule == null
                || !JobSchedule.STATUS_RUNNING.equals(jobSchedule.getJobStatus()))
            return;
        //获取任务调度器
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        LOGGER.debug(scheduler + "...........................................................add");
        //根据定时任务所在的名称和分组，来查找到对应的触发器的主键信息
        TriggerKey triggerKey = TriggerKey.triggerKey(jobSchedule.getJobName(), jobSchedule.getJobGroup());
        //根据触发器主键信息，获取对应的任务时间表达式触发器
        CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        //如果表达式触发器不存在则创建一个
        if (null == cronTrigger) {
            Class clazz = JobSchedule.CONCURRENT_IS.equals(jobSchedule.getIsConcurrent()) ? QuartzSchedulerFactory.class : QuartzJobFactoryDisallowConcurrentExecution.class;
        }

    }

    /**
     * 从已经运行的调度任务信息中删除任务调度信息
     *
     * @param jobSchedule 需要从任务调度器删除的调度任务信息
     */
    public void deleteRunningJobSchedule(JobSchedule jobSchedule) {

    }
}
