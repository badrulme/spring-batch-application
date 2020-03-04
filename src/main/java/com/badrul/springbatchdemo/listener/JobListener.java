package com.badrul.springbatchdemo.listener;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;

public class JobListener extends JobExecutionListenerSupport {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println("In Listener Before Run " + jobExecution.getJobInstance().getJobName() + "Job");
        jobExecution.getExecutionContext().putString("beforeJob", "beforeValue");

    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println("In Listener After Run Job. The value of before key " + jobExecution.getExecutionContext().getString("beforeJob"));
        if (jobExecution.getStatus().equals(BatchStatus.COMPLETED)) {
            System.out.println("Success");
        } else {
            System.out.println("Failed!");
        }
    }
}
