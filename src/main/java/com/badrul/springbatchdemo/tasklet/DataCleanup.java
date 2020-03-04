package com.badrul.springbatchdemo.tasklet;

import com.badrul.springbatchdemo.repository.EmployeeRepository;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class DataCleanup implements Tasklet {
    private EmployeeRepository employeeRepository;

    public DataCleanup(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        employeeRepository.deleteAll();
        return RepeatStatus.FINISHED;
    }
}
