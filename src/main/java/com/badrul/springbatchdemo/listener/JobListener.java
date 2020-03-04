package com.badrul.springbatchdemo.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;

public class JobListener extends JobExecutionListenerSupport {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println("In Listener Before Run Job");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println("In Listener After Run Job");
    }
}
