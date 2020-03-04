package com.badrul.springbatchdemo.listener;

import com.badrul.springbatchdemo.model.Employee;
import org.springframework.batch.core.ItemReadListener;


public class ReaderListener implements ItemReadListener<Employee> {

    @Override
    public void beforeRead() {
        System.out.println("Before Read Employee Item Read Listener ");
    }

    @Override
    public void afterRead(Employee employee) {
        System.out.println("After Read Employee Item Read Listener " + employee.toString());
    }

    @Override
    public void onReadError(Exception e) {
        System.out.println("Error on Read Employee Item Read Listener " + e.getMessage());
    }
}
