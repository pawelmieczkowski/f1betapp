package pl.salata.f1betapp.datapopulating;

import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import pl.salata.f1betapp.model.Circuit;
import pl.salata.f1betapp.model.QualificationResult;
import pl.salata.f1betapp.service.DriverService;
import pl.salata.f1betapp.service.GrandPrixService;
import pl.salata.f1betapp.service.TeamService;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class QualificationResultBatchConfig {

    private final String[] FIELD_NAMES = new String[]{
            "qualifyId", "raceId", "driverId", "constructorId", "number", "position", "q1", "q2", "q3"

    };

    private final String SOURCE_PATH = "/data/qualifying.csv";

    public JobBuilderFactory jobBuilderFactory;
    public StepBuilderFactory stepBuilderFactory;

    public ItemWriterFactory<QualificationResult> itemWriterFactory;

    private final TeamService teamService;
    private final DriverService driverService;
    private final GrandPrixService grandPrixService;

    @Bean
    public FlatFileItemReader<QualificationResultInput> qualificationResultReader() {
        return new FlatFileItemReaderBuilder<QualificationResultInput>()
                .name("qualificationResultReader")
                .resource(new ClassPathResource(SOURCE_PATH)).delimited()
                .names(FIELD_NAMES)
                .linesToSkip(1)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<QualificationResultInput>() {{
                    setTargetType(QualificationResultInput.class);
                }})
                .build();
    }

    @Bean
    public QualificationResultDataProcessor qualificationResultDataProcessor() {
        return new QualificationResultDataProcessor(grandPrixService, driverService, teamService);
    }

    @Bean
    public Job importQualificationResultJob(JobCompletionNotificationListener listener, Step stepQualificationResult) {
        return jobBuilderFactory.get("importQualificationResultJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(stepQualificationResult)
                .end()
                .build();
    }


    @Bean
    public Step stepQualificationResult() {
        return stepBuilderFactory.get("qualificationResultWriter")
                .<QualificationResultInput, QualificationResult>chunk(10)
                .reader(qualificationResultReader())
                .processor(qualificationResultDataProcessor())
                .writer(itemWriterFactory.getItemWriter())
                .build();
    }
}