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
import pl.salata.f1betapp.datapopulating.batch.dto.RaceResultInput;
import pl.salata.f1betapp.exception.EntityNotFoundException;
import pl.salata.f1betapp.model.Driver;
import pl.salata.f1betapp.model.RaceFinishStatus;
import pl.salata.f1betapp.model.RaceResult;
import pl.salata.f1betapp.model.Team;
import pl.salata.f1betapp.service.DriverService;
import pl.salata.f1betapp.service.RaceFinishStatusService;
import pl.salata.f1betapp.service.TeamService;


@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class RaceResultBatchConfig {

    private final String[] FIELD_NAMES = new String[]{
            "resultId", "raceId", "driverId", "constructorId", "number", "grid", "position", "positionText", "positionOrder",
            "points", "laps", "time", "milliseconds", "fastestLap", "rank", "fastestLapTime", "fastestLapSpeed", "statusId"
    };

    public JobBuilderFactory jobBuilderFactory;
    public StepBuilderFactory stepBuilderFactory;

    public ItemWriterFactory<RaceResult> itemWriterFactory;

    private final RaceFinishStatusService statusService;
    private final TeamService teamService;
    private final DriverService driverService;

    @Bean
    @StepScope
    public FlatFileItemReader<RaceResultInput> raceResultReader(@Value("#{jobParameters['dataSource']}") String dataPath) {
        return new FlatFileItemReaderBuilder<RaceResultInput>()
                .name("raceResultReader")
                .resource(new PathResource(dataPath)).delimited()
                .names(FIELD_NAMES)
                .linesToSkip(1)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(RaceResultInput.class);
                }})
                .build();
    }

    @Bean
    public ItemProcessor<RaceResultInput, RaceResult> raceResultDataProcessor() {
        return input -> {
            RaceResult raceResult = new RaceResult();

            InputProcessor.validateString(input.getPositionText()).ifPresent(raceResult::setFinishingPosition);
            InputProcessor.validateString(input.getLaps()).ifPresent(raceResult::setLaps);
            InputProcessor.validateString(input.getTime()).ifPresent(raceResult::setTime);
            InputProcessor.validateString(input.getFastestLapTime()).ifPresent(raceResult::setFastestLapTime);
            InputProcessor.validateString(input.getFastestLapSpeed()).ifPresent(raceResult::setFastestLapSpeed);
            InputProcessor.validateString(input.getNumber()).ifPresent(raceResult::setDriverNumber);

            InputProcessor.parseNumber(input.getResultId(), Long.class).ifPresent(raceResult::setId);
            InputProcessor.parseNumber(input.getGrid(), Integer.class).ifPresent(raceResult::setStartingGridPosition);
            InputProcessor.parseNumber(input.getPoints(), Float.class).ifPresent(raceResult::setPoints);
            InputProcessor.parseNumber(input.getMilliseconds(), Integer.class).ifPresent(raceResult::setTimeInMilliseconds);
            InputProcessor.parseNumber(input.getRank(), Integer.class).ifPresent(raceResult::setRanking);
            InputProcessor.parseNumber(input.getRaceId(), Long.class).ifPresent(raceResult::setGrandPrixId);

            InputProcessor.parseNumber(input.getStatusId(), Long.class)
                    .ifPresent(value -> {
                        try {
                            RaceFinishStatus status = statusService.findById(value);
                            raceResult.setStatus(status.getStatus());
                        } catch (EntityNotFoundException e) {
                            System.out.println(e.getMessage());
                        }
                    });

            InputProcessor.parseNumber(input.getConstructorId(), Long.class)
                    .ifPresent(value -> {
                        try {
                            Team team = teamService.getById(value);
                            raceResult.setTeamName(team.getName());
                        } catch (EntityNotFoundException e) {
                            System.out.println(e.getMessage());
                        }
                    });

            InputProcessor.parseNumber(input.getDriverId(), Long.class)
                    .ifPresent(value -> {
                        try {
                            Driver driver = driverService.getById(value);
                            String driverName = driver.getForename() + " " + driver.getSurname();
                            raceResult.setDriverId(value);
                            raceResult.setDriverName(driverName);
                        } catch (EntityNotFoundException e) {
                            System.out.println(e.getMessage()
                            );
                        }
                    });

            return raceResult;
        };
    }

    @Bean
    public Job importRaceResultJob(JobCompletionNotificationListener listener, Step stepRaceResult) {
        return jobBuilderFactory.get("importRaceResultJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(stepRaceResult)
                .end()
                .build();
    }

    @Bean
    public Step stepRaceResult() {
        return stepBuilderFactory.get("stepRaceResult")
                .<RaceResultInput, RaceResult>chunk(10)
                .reader(raceResultReader("OVERRIDDEN_BY_EXPRESSION"))
                .processor(raceResultDataProcessor())
                .writer(itemWriterFactory.getItemWriter())
                .build();
    }
}
