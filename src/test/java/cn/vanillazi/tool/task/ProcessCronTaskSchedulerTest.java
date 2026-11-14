package cn.vanillazi.tool.task;

import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import static org.junit.jupiter.api.Assertions.*;

class ProcessCronTaskSchedulerTest {

    static void main() throws SchedulerException {
        var scheduler= StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
        System.exit(0);
        //scheduler.shutdown();
    }
}