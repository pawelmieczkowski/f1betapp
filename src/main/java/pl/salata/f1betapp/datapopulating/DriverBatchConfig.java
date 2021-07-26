package pl.salata.f1betapp.datapopulating;

import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import pl.salata.f1betapp.model.Driver;
import pl.salata.f1betapp.model.RaceFinishStatus;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class DriverBatchConfig {

    private final String[] FIELD_NAMES = new String[]{
            "driverId", "driverRef", "number", "code", "forename", "surname", "dob", "nationality", "url"
    };

    public JobBuilderFactory jobBuilderFactory;
    public StepBuilderFactory stepBuilderFactory;

    private final ItemWriterFactory<Driver> itemWriterFactory;

    @Bean
    public FlatFileItemReader<DriverInput> driverReader() {
        return new FlatFileItemReaderBuilder<DriverInput>()
                .name("driverReader")
                .resource(new ClassPathResource("/data/drivers.csv")).delimited()
                .names(FIELD_NAMES)
                .linesToSkip(1)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<DriverInput>() {{
                    setTargetType(DriverInput.class);
                }})
                .build();
    }

    @Bean
    public DriverDataProcessor driverDataProcessor() {
        return new DriverDataProcessor();
    }

    @Bean
    public Job importDriverJob(JobCompletionNotificationListener listener, Step stepDriver) {
        return jobBuilderFactory.get("importDriverJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(stepDriver)
                .end()
                .build();
    }

    @Bean
    public Step stepDriver() {
        return stepBuilderFactory.get("stepDriver")
                .<DriverInput, Driver>chunk(10)
                .reader(driverReader())
                .processor(driverDataProcessor())
                .writer(itemWriterFactory.getItemWriter())
                .build();
    }
}
