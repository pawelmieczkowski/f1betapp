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
import pl.salata.f1betapp.model.RaceFinishStatus;

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
    @StepScope
    public FlatFileItemReader<RaceFinishStatusInput> raceFinishStatusReader(@Value("#{jobParameters['dataSource']}") String dataPath) {
        return new FlatFileItemReaderBuilder<RaceFinishStatusInput>()
                .name("raceFinishStatusReader")
                .resource(new PathResource(dataPath)).delimited()
                .names(FIELD_NAMES)
                .linesToSkip(1)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(RaceFinishStatusInput.class);
                }})
                .build();
    }

    @Bean
    public ItemProcessor<RaceFinishStatusInput, RaceFinishStatus> raceFinishStatusDataProcessor() {
        return input -> {
            RaceFinishStatus raceFinishStatus = new RaceFinishStatus();

            InputProcessor.parseNumber(input.getStatusId(), Long.class).ifPresent(raceFinishStatus::setId);
            InputProcessor.validateString(input.getStatus()).ifPresent(raceFinishStatus::setStatus);

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
                .reader(raceFinishStatusReader("OVERRIDDEN_BY_EXPRESSION"))
                .processor(raceFinishStatusDataProcessor())
                .writer(itemWriterFactory.getItemWriter())
                .build();
    }
}
