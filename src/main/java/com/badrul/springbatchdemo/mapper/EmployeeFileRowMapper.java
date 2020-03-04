package com.badrul.springbatchdemo.mapper;

import com.badrul.springbatchdemo.model.Employee;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

public class EmployeeFileRowMapper implements FieldSetMapper<Employee> {

    @Override
    public Employee mapFieldSet(FieldSet fieldSet) {
        Employee employee = new Employee();
        employee.setEmployeeId(fieldSet.readString("employeeId"));
        employee.setFirstName(fieldSet.readString("firstName"));
        employee.setLastName(fieldSet.readString("lastName"));
        employee.setEmail(fieldSet.readString("email"));
//        try {
            employee.setAge(fieldSet.readInt("age"));
//        } catch (Exception e) {
//        }
        return employee;
    }
}
