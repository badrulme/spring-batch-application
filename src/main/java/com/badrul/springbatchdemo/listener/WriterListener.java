package com.badrul.springbatchdemo.listener;

import com.badrul.springbatchdemo.model.Employee;
import org.springframework.batch.core.ItemWriteListener;

import java.util.List;

public class WriterListener implements ItemWriteListener<Employee> {
    @Override
    public void beforeWrite(List<? extends Employee> list) {
        System.out.println("Before Writer....");
        list.forEach(System.out::println);
    }

    @Override
    public void afterWrite(List<? extends Employee> list) {
        System.out.println("After Writer....");
    }

    @Override
    public void onWriteError(Exception e, List<? extends Employee> list) {
        System.out.println("Error on Writer....");
    }
}
