package com.badrul.springbatchdemo.processor;

import com.badrul.springbatchdemo.dto.EmployeeDTO;
import com.badrul.springbatchdemo.model.Employee;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class EmployeeProcessor implements ItemProcessor<Employee, Employee> {

    @Override
    public Employee process(Employee emp) throws Exception {
        Employee employee = new Employee();
        employee.setEmployeeId(emp.getEmployeeId()+new Random().nextInt(100000));
        employee.setFirstName(emp.getFirstName());
        employee.setLastName(emp.getLastName());
        employee.setEmail(emp.getEmail());
        employee.setAge(emp.getAge());
        System.out.println("inside processor " + employee.toString());
        return employee;
    }
}
