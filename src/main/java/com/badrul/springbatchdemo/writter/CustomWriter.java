package com.badrul.springbatchdemo.writter;

import com.badrul.springbatchdemo.model.Employee;
import com.badrul.springbatchdemo.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class CustomWriter implements ItemWriter<Employee> {

    private static final Logger logger = LoggerFactory.getLogger(CustomWriter.class);

    private EmployeeRepository employeeRepository;

    public CustomWriter(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void write(List<? extends Employee> employees) throws Exception {
        employeeRepository.saveAll(employees);
        logger.info("{} employees save in to database " + employees.size());
    }
}
