package com.badrul.springbatchdemo.listener;

import com.badrul.springbatchdemo.model.Employee;
import org.springframework.batch.core.ItemProcessListener;

public class ProcessListener implements ItemProcessListener<Employee, Employee> {
    @Override
    public void beforeProcess(Employee employee) {
        System.out.println("Before process " + employee.toString());
    }

    @Override
    public void afterProcess(Employee employee, Employee employee2) {
        System.out.println("After process " + employee2);
    }

    @Override
    public void onProcessError(Employee employee, Exception e) {
        System.out.println("Error on Processor Listener " + e.getMessage());
    }
}
