package com.badrul.springbatchdemo.job;

import com.badrul.springbatchdemo.repository.EmployeeRepository;
import com.badrul.springbatchdemo.tasklet.AgeGroupSummary;
import com.badrul.springbatchdemo.tasklet.DataCleanup;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobWithTasklet1 {
    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;
    private EmployeeRepository employeeRepository;

    @Autowired
    public JobWithTasklet1(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, EmployeeRepository employeeRepository) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.employeeRepository = employeeRepository;
    }

    @Qualifier(value = "jobTasklet1")
    @Bean
    public Job jobTasklet1() {
        return this.jobBuilderFactory.get("jobTasklet1")
                .start(step1JobWithTasklet1())
                .build();
    }

    @Bean
    public Step step1JobWithTasklet1() {
        return this.stepBuilderFactory.get("step1JobWithTasklet1")
                .tasklet(new DataCleanup(employeeRepository))
                .build();
    }

    @Bean
    public Step step1GroupReport() {
        return this.stepBuilderFactory.get("step1GroupReport")
                .tasklet(new AgeGroupSummary())
                .build();
    }
}
