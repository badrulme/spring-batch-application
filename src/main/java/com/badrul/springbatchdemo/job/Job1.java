package com.badrul.springbatchdemo.job;

import com.badrul.springbatchdemo.listener.JobListener;
import com.badrul.springbatchdemo.listener.ProcessListener;
import com.badrul.springbatchdemo.listener.ReaderListener;
import com.badrul.springbatchdemo.listener.WriterListener;
import com.badrul.springbatchdemo.mapper.EmployeeDBRowMapper;
import com.badrul.springbatchdemo.mapper.EmployeeFileRowMapper;
import com.badrul.springbatchdemo.model.Employee;
import com.badrul.springbatchdemo.processor.EmployeeProcessor;
import com.badrul.springbatchdemo.writter.CustomWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import javax.servlet.WriteListener;
import javax.sql.DataSource;

@Configuration
public class Job1 {

    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;
    private EmployeeProcessor employeeProcessor;
    private DataSource dataSource;
    private CustomWriter customWriter;

    @Autowired
    public Job1(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, EmployeeProcessor employeeProcessor, DataSource dataSource, CustomWriter customWriter) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.employeeProcessor = employeeProcessor;
        this.dataSource = dataSource;
        this.customWriter = customWriter;
    }

    @Bean
    public Job job1job() throws Exception {
        return this.jobBuilderFactory.get("job1")
                .start(job1Step1())
                .listener(jobListener())
                .build();
    }

    @Bean
    public Step job1Step1() throws Exception {
        return this.stepBuilderFactory.get("job1Step1")
                .<Employee, Employee>chunk(10)
//                .reader(employeeItemStreamReader())
                .reader(employeeFlatFileItemReader())
                .processor(employeeProcessor)
//                .writer(employeeJdbcBatchItemWriter())
                .writer(customWriter)
//                .writer(employeeItemWriter())
                .listener(readerListener())
                .listener(processListener())
                .listener(writeListener())
//                .faultTolerant().skipPolicy(jobSkipPolicy())
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    @StepScope
    Resource inputFileResource(@Value("#{jobParameters[fileName]}") final String fileName) throws Exception {
        return new ClassPathResource(fileName);
    }

    @Bean
    @StepScope
    public FlatFileItemReader<Employee> employeeFlatFileItemReader() throws Exception {
        FlatFileItemReader<Employee> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(inputFileResource(null));
        itemReader.setLineMapper(new DefaultLineMapper<Employee>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames("employeeId", "firstName", "lastName", "email", "age");
                setDelimiter(",");
            }});
            setFieldSetMapper(new EmployeeFileRowMapper());
        }});
        return itemReader;
    }

    private Resource outputResource = new FileSystemResource("output/employee-output.csv");

    @Bean
    public JdbcBatchItemWriter<Employee> employeeJdbcBatchItemWriter() {
        JdbcBatchItemWriter jdbcBatchItemWriter = new JdbcBatchItemWriter();
        jdbcBatchItemWriter.setDataSource(dataSource);
        jdbcBatchItemWriter.setSql("insert into employee (employee_id, first_name, last_name, email, age) values(:employeeId, :firstName, :lastName, :email, :age)");
        jdbcBatchItemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Employee>());
        return jdbcBatchItemWriter;
    }

    @Bean
    public ItemStreamReader<Employee> employeeItemStreamReader() {
        JdbcCursorItemReader<Employee> jdbcCursorItemReader = new JdbcCursorItemReader();
        jdbcCursorItemReader.setDataSource(dataSource);
        jdbcCursorItemReader.setSql("select * from employee");
        jdbcCursorItemReader.setRowMapper(new EmployeeDBRowMapper());
        return jdbcCursorItemReader;
    }

    @Bean
    public ItemWriter<Employee> employeeItemWriter() {
        FlatFileItemWriter<Employee> itemWriter = new FlatFileItemWriter<>();
        itemWriter.setResource(outputResource);
        itemWriter.setLineAggregator(new DelimitedLineAggregator<Employee>() {{
            setFieldExtractor(new BeanWrapperFieldExtractor<Employee>() {{
                setNames(new String[]{"employeeId", "firstName", "lastName", "email", "age"});
            }});
        }});
        itemWriter.setShouldDeleteIfExists(true);
        return itemWriter;
    }

    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor simpleAsyncTaskExecutor = new SimpleAsyncTaskExecutor();
        simpleAsyncTaskExecutor.setConcurrencyLimit(5);
        return simpleAsyncTaskExecutor;
    }

    @Bean
    public JobSkipPolicy jobSkipPolicy() {
        return new JobSkipPolicy();
    }

    @Bean
    public JobListener jobListener() {
        return new JobListener();
    }

    @Bean
    public ReaderListener readerListener() {
        return new ReaderListener();
    }

    @Bean
    public ProcessListener processListener() {
        return new ProcessListener();
    }

    @Bean
    public WriterListener writeListener() {
        return new WriterListener();
    }
}
