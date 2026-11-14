package cn.vanillazi.tool.task;

import cn.vanillazi.tool.StartupItem;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.List;

public class ProcessCronTaskScheduler {

    private final Scheduler scheduler;
    private final List<StartupItem> startupItems;

    public ProcessCronTaskScheduler(List<StartupItem> cronItems) throws SchedulerException {
        scheduler = StdSchedulerFactory.getDefaultScheduler();
        startupItems = cronItems;
    }

    public void start() throws SchedulerException {
        scheduler.start();
        initTasks();
    }

    private void initTasks() throws SchedulerException {
        for (StartupItem startupItem : startupItems) {
            initTask(startupItem);
        }
    }

    public void initTask(StartupItem startupItem) throws SchedulerException {
        var trigger=TriggerBuilder.newTrigger().withIdentity(startupItem.getName())
                .withSchedule(CronScheduleBuilder.cronSchedule(startupItem.getCron()))
                .build();
        var data=new JobDataMap();
        data.put("startupItem",startupItem);
        var job=JobBuilder.newJob()
                .ofType(ProcessCronTask.class)
                .setJobData(data)
                .build();
        scheduler.scheduleJob(job,trigger);
    }

    public void shutdown() throws SchedulerException {
        scheduler.shutdown();
    }
}
