package com.badrul.springbatchdemo.job;

import com.badrul.springbatchdemo.tasklet.AgeGroupSummary;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobWithTasklet2 {
    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;

    public JobWithTasklet2(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job jobTasklet2() {
        return this.jobBuilderFactory.get("jobTasklet2")
                .start(step1JobTasklet2())
                .build();
    }

    @Bean
    public Step step1JobTasklet2() {
        return this.stepBuilderFactory.get("step1JobTasklet2")
                .tasklet(new AgeGroupSummary())
                .build();
    }

}
