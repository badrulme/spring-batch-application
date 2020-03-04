package com.badrul.springbatchdemo.tasklet;

import com.badrul.springbatchdemo.model.Employee;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.util.CollectionUtils;
import sun.text.CollatorUtilities;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AgeGroupSummary implements Tasklet {
    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        System.out.println("Inside tasklet group report");
        try (Stream<String> employees = Files.lines(Paths.get("src/main/resources/employees.csv"))) {
            List<Employee> employeeList = employees.map((srtData) -> srtData.split(",")).map(AgeGroupSummary::employeeMapper).collect(Collectors.toList());

            if (!CollectionUtils.isEmpty(employeeList)) {
                Map<Integer, Long> ageGroup = employeeList.stream().collect(Collectors.groupingBy(Employee::getAge, Collectors.counting()));
                System.out.println(ageGroup);
            }
        } catch (Exception e) {
        }
        return RepeatStatus.FINISHED;
    }

    private static Employee employeeMapper(String[] record) {
        Employee employee = new Employee();
        employee.setEmployeeId(record[0]);
        employee.setFirstName(record[1]);
        employee.setLastName(record[2]);
        employee.setEmail(record[3]);
        employee.setAge(Integer.parseInt(record[4]));
        return employee;
    }
}
