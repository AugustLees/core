package com.august.controller;

import com.august.common.RetObj;
import com.august.domain.hibernate.JobSchedule;
import com.august.service.JobScheduleService;
import com.august.common.SpringUtils;
import org.quartz.CronScheduleBuilder;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.controller
 * Author: August
 * Update: August(2015/9/24)
 * Description: 任务调度控制器
 * 该控制器用于实现任务调度，实现定时任务的添加，修改，删除，暂停等操作
 */
@Controller
@RequestMapping("/schedule")
public class JobScheduleController {
    //定义日志记录器
    private static final Logger LOGGER = LoggerFactory.getLogger(JobScheduleController.class);

    //引入任务调度服务类实例
    @Autowired
    private JobScheduleService jobScheduleService;

    /**
     * 任务调度列表查询
     * 查询分页对象
     *
     * @param request 请求信息
     * @return 响应结果
     */
    @RequestMapping("/jobScheduleList")
    @ResponseBody
    public Page<JobSchedule> jobScheduleList(HttpServletRequest request) {
        /**
         *  创建一个分页对象   （注意：0代表的是第一页，5代表每页的大小,后两个参数不写即为默认排序）
         *  Direction：为一个枚举类，定义了DESC和ASC排序顺序
         *  id：结果集根据id来进行DESC降序排序
         *  想自己实现的话，最好继承他这个类，来定义一些个性的方法
         */
        PageRequest pageRequest = new PageRequest(0, 5, Sort.Direction.DESC, "id");
        JobSchedule jobSchedule=new JobSchedule();
        jobSchedule.setJobName("测试任务一");
        jobSchedule.setJobGroup("测试任务zu一");
        Page<JobSchedule>  jobSchedulePage = jobScheduleService.getJobScheduleList(jobSchedule, pageRequest);
        // 打印分页详情
        System.out.println("查询结果：共"+jobSchedulePage.getTotalElements()+"条数据，每页显示"+jobSchedulePage.getSize()+"条，共"+jobSchedulePage.getTotalPages()+"页，当前第"+(jobSchedulePage.getNumber()+1)+"页！");
        // 打印结果集的内容
        System.out.println(jobSchedulePage.getContent());
        return jobSchedulePage;
    }

    /**
     * 添加任务调度信息
     *
     * @return 添加信息结果
     */
    @RequestMapping("addJobSchedule")
    @ResponseBody
    public RetObj addJobSchedule(HttpServletRequest request, JobSchedule jobSchedule) {
        RetObj retObj = new RetObj();
        retObj.setFlag(false);
        try {
            /**
             * 获取需要添加调度任务的任务表达式
             */
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(jobSchedule.getCronExpression());
        } catch (Exception e) {
            retObj.setMsg("cron表达式有误，不能被解析！");
            return retObj;
        }
        Object object = null;
        try {
            //如果springID不为空，则查找该SpringID对应的bean实例信息
            if (!StringUtils.isEmpty(jobSchedule.getSpringId())) {
                //根据指定的beanID进行实例化
                object = SpringUtils.getBean(jobSchedule.getSpringId());
            } else {
                //如果不存在则根据指定的包名+类名进行类的实例化
                Class clazz = Class.forName(jobSchedule.getBeanClass());
                object = clazz.newInstance();
            }
        } catch (BeansException e) {
            retObj.setMsg("SpringID为[" + jobSchedule.getSpringId() + "]的Bean不存在，请重新确认！");
            e.printStackTrace();
            return retObj;
        } catch (ClassNotFoundException e) {
            retObj.setMsg("类路径[" + jobSchedule.getBeanClass() + "]错误，请重新确认！");
            e.printStackTrace();
            return retObj;
        } catch (InstantiationException e) {
            retObj.setMsg("[" + jobSchedule.getBeanClass() + "]类实例化出现异常，请重新确认！");
            e.printStackTrace();
            return retObj;
        } catch (IllegalAccessException e) {
            retObj.setMsg("无法访问[" + jobSchedule.getBeanClass() + "]类，请重新确认！");
            e.printStackTrace();
            return retObj;
        }

        //对目标类进行存在性校验
        if (object == null) {
            retObj.setMsg("找不到[" + jobSchedule.getBeanClass() + "]类，请重新确认！");
        } else {
            //找到类后找到对应的方法信息,找不到对应方法则返回错误信息。
            Class clazz = object.getClass();
            Method method = null;
            try {
                method = clazz.getMethod(jobSchedule.getMethodName(), null);
            } catch (NoSuchMethodException e) {
                retObj.setMsg("[" + jobSchedule.getBeanClass() + "]类中不存在方法[" + jobSchedule.getMethodName() + "]，请重新确认！");
                e.printStackTrace();
                return retObj;
            }
            if (method == null) {
                retObj.setMsg("[" + jobSchedule.getBeanClass() + "]类中不存在方法[" + jobSchedule.getMethodName() + "]，请重新确认！");
                return retObj;
            }
        }
        try {
            //添加数据信息到数据库
            jobScheduleService.addJobSchedule(jobSchedule);
        } catch (Exception e) {
            retObj.setFlag(false);
            retObj.setMsg("保存失败，检查 name group 组合是否有重复！");
            return retObj;
        }
        return retObj;
    }

    /**
     * 修改任务执行表达式
     *
     * @param id   需要修改的数据记录的ID
     * @param cron 修改后的任务执行表达式
     * @return 执行结果
     */
    @RequestMapping("/updateCron")
    @ResponseBody
    public RetObj updateCron(Long id, String cron) {
        RetObj retObj = new RetObj();
        retObj.setFlag(false);
        //对选择cron表达式进行校验
        try {
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
        } catch (Exception e) {
            retObj.setMsg("cron表达式有误，不能被解析！");
            return retObj;
        }//更新时间表达式信息
        try {
            jobScheduleService.updateCron(id, cron);
        } catch (SchedulerException e) {
            retObj.setMsg("cron表达式更新失败！");
            return retObj;
        }
        retObj.setFlag(true);
        return retObj;
    }

    /**
     * 改变任务状态
     *
     * @param id  需要改变任务状态的数据ID
     * @param cmd 进行更改操作的命令
     * @return 更改结果
     */
    @RequestMapping("/changeJobScheduleStatus")
    @ResponseBody
    public RetObj changeJobScheduleStatus(long id, String cmd) {
        RetObj retObj = new RetObj();
        try {
            jobScheduleService.changeJobScheduleStatus(id, cmd);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return retObj;
    }
}
