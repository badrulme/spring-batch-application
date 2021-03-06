package com.badrul.springbatchdemo.runner;

import com.badrul.springbatchdemo.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class JobRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobRunner.class);

    private JobLauncher jobLauncher;
    private Job job1;
    private Job taskletJob1;
    private Job taskletJob2;

    @Autowired
    public JobRunner(JobLauncher jobLauncher, @Qualifier("job1") Job job, @Qualifier("jobTasklet1") Job taskletJob1, @Qualifier("jobTasklet2") Job taskletJob2) {
        this.jobLauncher = jobLauncher;
        this.job1 = job;
        this.taskletJob1 = taskletJob1;
        this.taskletJob2 = taskletJob2;
    }

    @Async
    public void runBatchJob() {
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        jobParametersBuilder.addString(Constants.FILE_NAME_CONTEXT_KEY, "employees.csv");
        jobParametersBuilder.addDate("date", new Date(), true);
        runJob(job1, jobParametersBuilder.toJobParameters());
    }

    @Async
    public void runTasklet1Job() {
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        jobParametersBuilder.addDate("date", new Date(), true);
        runJob(taskletJob1, jobParametersBuilder.toJobParameters());
    }

    @Async
    public void runGroupReport() {
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        jobParametersBuilder.addDate("date", new Date());
        runJob(taskletJob2, jobParametersBuilder.toJobParameters());
    }

    public void runJob(Job job, JobParameters jobParameters) {
        try {
            JobExecution jobExecution = jobLauncher.run(job, jobParameters);
        } catch (JobInstanceAlreadyCompleteException e) {
            LOGGER.info("Job with fileName{} is already completed", jobParameters.getParameters().get("fileName"));
        } catch (JobRestartException e) {
            LOGGER.info("Job with fileName{} was not interested", jobParameters.getParameters().get("fileName"));
        } catch (JobParametersInvalidException e) {
            LOGGER.info("Invalid job parameters", jobParameters.getParameters().get("fileName"));
        } catch (JobExecutionAlreadyRunningException e) {
            LOGGER.info("Job with fileName{} is already running", jobParameters.getParameters().get("fileName"));
        }
    }
}
