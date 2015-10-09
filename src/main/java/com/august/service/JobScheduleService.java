package com.august.service;

import com.august.dao.repositories.JobScheduleRepository;
import com.august.domain.hibernate.JobSchedule;
import com.august.utils.JPATx;
import com.august.utils.QuartzJobFactoryDisallowConcurrentExecution;
import com.august.utils.QuartzSchedulerFactory;
import com.august.utils.StaticConstant;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
     * 获取所有的数据库中调度任务信息列表
     *
     * @param jobSchedule 需要查询的参数集合
     * @return 查询到的任务信息列表
     */
    public List<JobSchedule> getJobScheduleList(JobSchedule jobSchedule) {
        return (List<JobSchedule>) jobScheduleRepository.findAll();
    }

    /**
     * 根据一个分页对象查询JobSchedule集合（还可以添加一个Store排序属性）
     * PageRequest    是spring自己封装的请求分页类，实现了Pageable接口，包涵从请求中获得的分页属性（当前页和大小）和获取方法
     * 通过调用分页方法，返回一个Page<>一个泛型集合的分页对象，里面包涵了通过查询计算出的各个属性和结果集
     * 详细类结构和属性请参阅源码
     *
     * @param jobSchedule 需要查询的参数集合
     * @param pageRequest 分页信息
     * @return 查询到的带有分页的任务信息列表
     */
    public Page<JobSchedule> getJobScheduleList(JobSchedule jobSchedule, PageRequest pageRequest) {
        Specification<JobSchedule> specification = buildSpecification(jobSchedule);
        return  jobScheduleRepository.findAll(specification, pageRequest);
    }

    /**
     * 动态创建条件查询参数请求
     * 创建动态查询条件组合.
     * @param jobSchedule
     * @return
     */
    private Specification<JobSchedule> buildSpecification(JobSchedule jobSchedule) {
//        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
//        filters.put("user.id", new SearchFilter("user.id", Operator.EQ, userId));
//        Specification<Task> spec = DynamicSpecifications.bySearchFilter(filters.values(), Task.class);
//        return spec;
        return new Specification<JobSchedule>() {
                @Override
                public Predicate toPredicate(Root<JobSchedule> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    Path<String> namePath = root.get("name");
                    Path<String> nicknamePath = root.get("nickname");
                    /**
                     * 连接查询条件, 不定参数，可以连接0..N个查询条件
                     */
                    criteriaQuery.where(criteriaBuilder.like(namePath, "%李%"), criteriaBuilder.like(nicknamePath, "%王%")); //这里可以设置任意条查询条件

                    return null;
                }
            };
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
        jobScheduleRepository.save(jobSchedule);

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
        jobScheduleRepository.save(jobSchedule);
    }

    //////////////////////////应用程序初始化时加载如下信息//////////////////////////////
    @PostConstruct
    public void init() throws Exception {
        System.out.println("========================将所有任务加载到定时任务中");
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        //获取所有定时任务信息
        List<JobSchedule> jobSchedules = (List<JobSchedule>) jobScheduleRepository.findAll();
        //将定时任务信息添加到调度器中
        for (JobSchedule jobSchedule : jobSchedules) {
            addRunningJobSchedule(jobSchedule);
        }
    }
    //////////////////////////应用程序初始化时加载信息结束//////////////////////////////

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
        LOGGER.debug("{}...........................................................add", scheduler);
        //根据定时任务所在的名称和分组，来查找到对应的触发器的主键信息
        TriggerKey triggerKey = TriggerKey.triggerKey(jobSchedule.getJobName(), jobSchedule.getJobGroup());
        //根据触发器主键信息，获取对应的任务时间表达式触发器
        CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        //如果表达式触发器不存在则创建一个
        if (null == cronTrigger) {
            Class clazz = JobSchedule.CONCURRENT_IS.equals(jobSchedule.getIsConcurrent())
                    ? QuartzSchedulerFactory.class : QuartzJobFactoryDisallowConcurrentExecution.class;
            //以任务名称和任务分组为主键，创建一个联合唯一的详细任务信息
            JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(jobSchedule.getJobName(), jobSchedule.getJobGroup()).build();
            //将任务调度信息存放到任务细节数据集合中
            jobDetail.getJobDataMap().put(StaticConstant.SCHEDULE_JOB_NAME, jobSchedule);
            //根据任务执行时间表达式，创建表达式调度器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(jobSchedule.getCronExpression());

            //以任务名称和任务分组为主键，创建一个联合唯一的带有执行时间的触发器
            cronTrigger = TriggerBuilder.newTrigger().
                    withIdentity(jobSchedule.getJobName(), jobSchedule.getJobGroup())
                    .withSchedule(scheduleBuilder).build();
            //将调度器中创建调度任务信息
            scheduler.scheduleJob(jobDetail, cronTrigger);
        } else {
            //如果表达式trigger存在，那么需要更新相应的任务执行时间表达式信息
            //创建表达式调度器
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(jobSchedule.getCronExpression());
            //按新的cronExpression表达式重新构建trigger
            cronTrigger = cronTrigger.getTriggerBuilder().
                    withIdentity(triggerKey).withSchedule(cronScheduleBuilder).build();
            // 按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, cronTrigger);
        }
    }

    /**
     * 从已经运行的调度任务信息中删除任务调度信息
     *
     * @param jobSchedule 需要从任务调度器删除的调度任务信息
     */
    public void deleteRunningJobSchedule(JobSchedule jobSchedule) throws SchedulerException {
        //获取任务调度器
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        //根据任务名和任务分组获取对应的任务信息主键
        JobKey jobKey = JobKey.jobKey(jobSchedule.getJobName(), jobSchedule.getJobGroup());
        //删除任务信息
        scheduler.deleteJob(jobKey);
    }


    /**
     * 获取所有存在于调度信息列表中的定时任务信息
     *
     * @return
     * @throws SchedulerException
     */
    public List<JobSchedule> getAllScheduleJob() throws SchedulerException {
        //获取调度器
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        //获取调度器中所有分组匹配后的信息
        GroupMatcher<JobKey> groupMatcher = GroupMatcher.anyJobGroup();
        //根据匹配信息获取任务信息主键
        Set<JobKey> jobKeys = scheduler.getJobKeys(groupMatcher);
        List<JobSchedule> jobSchedules = new ArrayList<JobSchedule>();
        //遍历任务信息主键，并将调度信息中数据转化为任务调度实体信息。
        for (JobKey jobKey : jobKeys) {
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
            for (Trigger trigger : triggers) {
                JobSchedule jobSchedule = new JobSchedule();
                jobSchedule.setJobName(jobKey.getName());
                jobSchedule.setJobGroup(jobKey.getGroup());
                jobSchedule.setDescription("触发器：" + trigger.getKey());
                Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                jobSchedule.setJobStatus(triggerState.name());
                if (trigger instanceof CronTrigger) {
                    CronTrigger cronTrigger = (CronTrigger) trigger;
                    String cronExpression = cronTrigger.getCronExpression();
                    jobSchedule.setCronExpression(cronExpression);
                }
                jobSchedules.add(jobSchedule);
            }
        }
        return jobSchedules;
    }


    /**
     * 获取所有在调度其中执行的任务信息
     *
     * @return
     */
    public List<JobSchedule> getRunningJob() throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
        List<JobSchedule> jobList = new ArrayList<JobSchedule>(executingJobs.size());
        for (JobExecutionContext executingJob : executingJobs) {
            JobSchedule job = new JobSchedule();
            JobDetail jobDetail = executingJob.getJobDetail();
            JobKey jobKey = jobDetail.getKey();
            Trigger trigger = executingJob.getTrigger();
            job.setJobName(jobKey.getName());
            job.setJobGroup(jobKey.getGroup());
            job.setDescription("触发器:" + trigger.getKey());
            Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
            job.setJobStatus(triggerState.name());
            if (trigger instanceof CronTrigger) {
                CronTrigger cronTrigger = (CronTrigger) trigger;
                String cronExpression = cronTrigger.getCronExpression();
                job.setCronExpression(cronExpression);
            }
            jobList.add(job);
        }
        return jobList;
    }


    /**
     * 暂停一个job任务信息
     *
     * @param jobSchedule 需要暂停的任务信息实例
     */
    public void pauseJob(JobSchedule jobSchedule) throws SchedulerException {
        //获取任务调度器
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        //根据任务名和任务分组获取对应的任务信息主键
        JobKey jobKey = JobKey.jobKey(jobSchedule.getJobName(), jobSchedule.getJobGroup());
        //暂停相应任务信息
        scheduler.pauseJob(jobKey);
    }

    /**
     * 恢复一个job
     *
     * @param jobSchedule
     * @throws SchedulerException
     */
    public void resumeJob(JobSchedule jobSchedule) throws SchedulerException {
        //获取任务调度器
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        //根据任务名和任务分组获取对应的任务信息主键
        JobKey jobKey = JobKey.jobKey(jobSchedule.getJobName(), jobSchedule.getJobGroup());
        //恢复任务信息
        scheduler.resumeJob(jobKey);
    }

    /**
     * 立即执行job
     *
     * @param jobSchedule
     * @throws SchedulerException
     */
    public void runAJobNow(JobSchedule jobSchedule) throws SchedulerException {
        //获取任务调度器
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        //根据任务名和任务分组获取对应的任务信息主键
        JobKey jobKey = JobKey.jobKey(jobSchedule.getJobName(), jobSchedule.getJobGroup());
        //恢复任务信息
        scheduler.triggerJob(jobKey);
    }

    /**
     * 修改调度器中正在执行的任务执行时间表达式
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
}
