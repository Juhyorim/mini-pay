package com.lime.minipay.service;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

@Service
public class MainAccountScheduledTasks {
    private final TaskScheduler taskScheduler;
    private final MainAccountService mainAccountService;
    private String dayChargedCron = "0 0 0 * * ?"; //테스트용: 0 0/1 * * * ?

    public MainAccountScheduledTasks(TaskScheduler taskScheduler, MainAccountService mainAccountService) {
        this.taskScheduler = taskScheduler;
        this.mainAccountService = mainAccountService;

        initDayChargedTask();
    }

    public void initDayChargedTask() {
        Runnable task = () -> mainAccountService.initDayCharged();

        taskScheduler.schedule(task, getCronTrigger(dayChargedCron));
    }

    private Trigger getCronTrigger(String cron) {
        return new CronTrigger(cron);
    }

//    @Scheduled(cron = "0 42 16 * * *", zone="Asia/Seoul")
//    public void run() {
//        System.out.println("hi");
//    }
//
//    cron = "0 0 0 * * *", zone="Asia/Seoul"
}