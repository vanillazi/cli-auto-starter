package cn.vanillazi.tool.task;

import cn.vanillazi.tool.CommandProcessListener;
import cn.vanillazi.tool.ProcessContext;
import cn.vanillazi.tool.StartupItem;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ProcessCronTask implements Job {

    private static final Logger logger= LoggerFactory.getLogger(ProcessCronTask.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        var data=context.getJobDetail().getJobDataMap();
        var startupItem=(StartupItem)data.get("startupItem");
        logger.info("schedule cli {} at {}",startupItem.getName(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        var processContext=new ProcessContext(startupItem, new CommandProcessListener() {
            @Override
            public void onStarted() {
                 logger.info("schedule {} started",startupItem.getName());
            }

            @Override
            public void onFinished() {
                logger.info("schedule {} finished",startupItem.getName());
            }

            @Override
            public void onError(String msg, Throwable cause) {
               logger.error("schedule {} error {}",startupItem.getName(),msg,cause);
            }
        });
        processContext.start();
    }
}
