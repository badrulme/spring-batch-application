package com.badrul.springbatchdemo.controller;

import com.badrul.springbatchdemo.runner.JobRunner;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/run")
public class JobController {

    private JobRunner jobRunner;

    public JobController(JobRunner jobRunner) {
        this.jobRunner = jobRunner;
    }

    @GetMapping("/job")
    public String runJob() {
        jobRunner.runBatchJob();
        return "Job submitted successfully.";
    }

    @GetMapping("/job/tasklet/clean")
    public String runTaskletCleanupJob() {
        jobRunner.runTasklet1Job();
        return "Tasklet Job submitted successfully.";
    }

    @GetMapping("/job/tasklet/group-report")
    public String runTaskletGroupReport() {
        jobRunner.runGroupReport();
        return "Tasklet Job submitted successfully.";
    }

}
