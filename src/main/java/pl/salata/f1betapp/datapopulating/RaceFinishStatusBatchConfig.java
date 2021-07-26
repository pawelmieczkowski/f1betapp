package pl.salata.f1betapp.datapopulating;

import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import pl.salata.f1betapp.model.Circuit;
import pl.salata.f1betapp.model.GrandPrix;
import pl.salata.f1betapp.model.RaceFinishStatus;
import pl.salata.f1betapp.service.CircuitService;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class RaceFinishStatusBatchConfig {

    private final String[] FIELD_NAMES = new String[]{
            "statusId", "status"
    };

    public JobBuilderFactory jobBuilderFactory;
    public StepBuilderFactory stepBuilderFactory;

    public ItemWriterFactory<RaceFinishStatus> itemWriterFactory;

    @Bean
    public FlatFileItemReader<RaceFinishStatusInput> raceFinishStatusReader() {
        return new FlatFileItemReaderBuilder<RaceFinishStatusInput>()
                .name("raceFinishStatusReader")
                .resource(new ClassPathResource("/data/status.csv")).delimited()
                .names(FIELD_NAMES)
                .linesToSkip(1)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<RaceFinishStatusInput>() {{
                    setTargetType(RaceFinishStatusInput.class);
                }})
                .build();
    }

    @Bean
    public ItemProcessor<RaceFinishStatusInput, RaceFinishStatus> raceFinishStatusDataProcessor() {
        return input -> {
            RaceFinishStatus raceFinishStatus = new RaceFinishStatus();

            InputProcessor.parseNumber(input.getStatusId(), Long.class).ifPresent(raceFinishStatus::setId);
            raceFinishStatus.setStatus(InputProcessor.validateString(input.getStatus()));

            return raceFinishStatus;
        };
    }

    @Bean
    public Job importRaceFinishStatusJob(JobCompletionNotificationListener listener, Step stepRaceFinishStatus) {
        return jobBuilderFactory.get("importRaceFinishStatusJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(stepRaceFinishStatus)
                .end()
                .build();
    }


    @Bean
    public Step stepRaceFinishStatus() {
        return stepBuilderFactory.get("stepRaceFinishStatus")
                .<RaceFinishStatusInput, RaceFinishStatus>chunk(10)
                .reader(raceFinishStatusReader())
                .processor(raceFinishStatusDataProcessor())
                .writer(itemWriterFactory.getItemWriter())
                .build();
    }
}
