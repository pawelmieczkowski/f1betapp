package pl.salata.f1betapp.datapopulating.batch;

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
import pl.salata.f1betapp.datapopulating.batch.dto.QualificationResultInput;
import pl.salata.f1betapp.exception.EntityNotFoundException;
import pl.salata.f1betapp.model.Driver;
import pl.salata.f1betapp.model.QualificationResult;
import pl.salata.f1betapp.model.Team;
import pl.salata.f1betapp.service.DriverService;
import pl.salata.f1betapp.service.TeamService;

@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class QualificationResultBatchConfig {

    private final String[] FIELD_NAMES = new String[]{
            "qualifyId", "raceId", "driverId", "constructorId", "number", "position", "q1", "q2", "q3"
    };

    public JobBuilderFactory jobBuilderFactory;
    public StepBuilderFactory stepBuilderFactory;

    public ItemWriterFactory<QualificationResult> itemWriterFactory;

    private final TeamService teamService;
    private final DriverService driverService;

    @Bean
    @StepScope
    public FlatFileItemReader<QualificationResultInput> qualificationResultReader(@Value("#{jobParameters['dataSource']}") String dataPath) {
        return new FlatFileItemReaderBuilder<QualificationResultInput>()
                .name("qualificationResultReader")
                .resource(new PathResource(dataPath)).delimited()
                .names(FIELD_NAMES)
                .linesToSkip(1)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(QualificationResultInput.class);
                }})
                .build();
    }

    @Bean
    public ItemProcessor<QualificationResultInput, QualificationResult> qualificationResultDataProcessor() {
        return input -> {
            QualificationResult result = new QualificationResult();

            InputProcessor.parseNumber(input.getQualifyId(), Long.class).ifPresent(result::setId);
            InputProcessor.parseNumber(input.getPosition(), Integer.class).ifPresent(result::setResult);

            InputProcessor.validateString(input.getQ1()).ifPresent(result::setQ1time);
            InputProcessor.validateString(input.getQ2()).ifPresent(result::setQ2time);
            InputProcessor.validateString(input.getQ3()).ifPresent(result::setQ3time);
            InputProcessor.validateString(input.getNumber()).ifPresent(result::setDriverNumber);

            InputProcessor.parseNumber(input.getRaceId(), Long.class).ifPresent(result::setGrandPrixId);

            InputProcessor.parseNumber(input.getDriverId(), Long.class)
                    .ifPresent(value -> {
                        try {
                            Driver driver = driverService.getById(value);
                            String driverName = driver.getForename() + " " + driver.getSurname();
                            result.setDriverName(driverName);
                            result.setDriverId(value);
                        } catch (EntityNotFoundException e) {
                            System.out.println(e.getMessage());
                        }
                    });

            InputProcessor.parseNumber(input.getConstructorId(), Long.class)
                    .ifPresent(value -> {
                        try {
                            Team team = teamService.getById(value);
                            result.setTeamName(team.getName());
                        } catch (EntityNotFoundException e) {
                            System.out.println(e.getMessage());
                        }
                    });
            return result;
        };
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
                .reader(qualificationResultReader("OVERRIDDEN_BY_EXPRESSION"))
                .processor(qualificationResultDataProcessor())
                .writer(itemWriterFactory.getItemWriter())
                .build();
    }
}
