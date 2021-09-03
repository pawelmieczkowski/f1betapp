package pl.salata.f1betapp.datapopulating;

import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.PathResource;
import pl.salata.f1betapp.model.Driver;

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
    @StepScope
    public FlatFileItemReader<DriverInput> driverReader(@Value("#{jobParameters['dataSource']}") String dataPath) {
        return new FlatFileItemReaderBuilder<DriverInput>()
                .name("driverReader")
                .resource(new PathResource(dataPath)).delimited()
                .names(FIELD_NAMES)
                .linesToSkip(1)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(DriverInput.class);
                }})
                .build();
    }

    @Bean
    public ItemProcessor<DriverInput, Driver> driverDataProcessor() {
        return input -> {
            Driver driver = new Driver();

            InputProcessor.parseNumber(input.getDriverId(), Long.class).ifPresent(driver::setId);

            driver.setDriverCode(InputProcessor.validateString(input.getCode()));
            driver.setForename(InputProcessor.validateString(input.getForename()));
            driver.setSurname(InputProcessor.validateString(input.getSurname()));
            driver.setDriverNumber(InputProcessor.validateString(input.getNumber()));

            driver.setNationality(InputProcessor.validateString(input.getNationality()));
            driver.setUrl(InputProcessor.validateString(input.getUrl()));

            driver.setDateOfBirth(InputProcessor.parseDate(input.getDob()));

            return driver;
        };
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
                .reader(driverReader("OVERRIDDEN_BY_EXPRESSION"))
                .processor(driverDataProcessor())
                .writer(itemWriterFactory.getItemWriter())
                .build();
    }
}
