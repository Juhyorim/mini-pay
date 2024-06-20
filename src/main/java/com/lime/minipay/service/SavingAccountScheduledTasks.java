package com.lime.minipay.service;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

@Service
public class SavingAccountScheduledTasks {
    private final TaskScheduler taskScheduler;
    private final SavingAccountService savingAccountService;
    private String interestCron = "0 0 4 * * ?"; //테스트용: 0 0/1 * * * ?

    public SavingAccountScheduledTasks(TaskScheduler taskScheduler, SavingAccountService savingAccountService) {
        this.taskScheduler = taskScheduler;
        this.savingAccountService = savingAccountService;

        addInterestTask();
    }

    public void addInterestTask() {
        Runnable task = () -> savingAccountService.addInterestForAllMember();

        taskScheduler.schedule(task, getCronTrigger(interestCron));
    }

    private Trigger getCronTrigger(String cron) {
        return new CronTrigger(cron);
    }
}