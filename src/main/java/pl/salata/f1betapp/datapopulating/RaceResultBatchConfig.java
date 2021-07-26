package pl.salata.f1betapp.datapopulating;

import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import pl.salata.f1betapp.model.*;
import pl.salata.f1betapp.service.DriverService;
import pl.salata.f1betapp.service.GrandPrixService;
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
    //TODO: exclude that variable in every config class
    private final String SOURCE_PATH = "/data/results.csv";

    public JobBuilderFactory jobBuilderFactory;
    public StepBuilderFactory stepBuilderFactory;

    public ItemWriterFactory<RaceResult> itemWriterFactory;

    private final RaceFinishStatusService statusService;
    private final TeamService teamService;
    private final DriverService driverService;
    private final GrandPrixService grandPrixService;

    @Bean
    public FlatFileItemReader<RaceResultInput> raceResultReader() {
        return new FlatFileItemReaderBuilder<RaceResultInput>()
                .name("raceResultReader")
                .resource(new ClassPathResource(SOURCE_PATH)).delimited()
                .names(FIELD_NAMES)
                .linesToSkip(1)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<RaceResultInput>() {{
                    setTargetType(RaceResultInput.class);
                }})
                .build();
    }

    @Bean
    public ItemProcessor<RaceResultInput, RaceResult> raceResultDataProcessor() {
        return input -> {
            RaceResult raceResult = new RaceResult();

            raceResult.setFinishingPosition(InputProcessor.validateString(input.getPositionText()));
            raceResult.setLaps(InputProcessor.validateString(input.getLaps()));
            raceResult.setTime(InputProcessor.validateString(input.getTime()));
            raceResult.setFastestLapTime(InputProcessor.validateString(input.getFastestLapTime()));
            raceResult.setFastestLapSpeed(InputProcessor.validateString(input.getFastestLapSpeed()));

            InputProcessor.parseNumber(input.getResultId(), Long.class).ifPresent(raceResult::setId);
            InputProcessor.parseNumber(input.getGrid(), Integer.class).ifPresent(raceResult::setStartingGridPosition);
            InputProcessor.parseNumber(input.getPoints(), Float.class).ifPresent(raceResult::setPoints);
            InputProcessor.parseNumber(input.getMilliseconds(), Integer.class).ifPresent(raceResult::setTimeInMilliseconds);
            InputProcessor.parseNumber(input.getRank(), Integer.class).ifPresent(raceResult::setRanking);

            InputProcessor.parseNumber(input.getStatusId(), Long.class)
                    .ifPresent(value -> {
                        RaceFinishStatus status = statusService.findById(value);
                        raceResult.setStatus(status.getStatus());
                    });

            InputProcessor.parseNumber(input.getConstructorId(), Long.class)
                    .ifPresent(value -> {
                        Team team = teamService.findById(value);
                        raceResult.setTeamName(team.getName());
                    });

            InputProcessor.parseNumber(input.getDriverId(), Long.class)
                    .ifPresent(value -> {
                        Driver driver = driverService.findById(value);
                        String driverName = driver.getForename() + " " + driver.getSurname();
                        raceResult.setDriverName(driverName);
                        raceResult.setDriverNumber(driver.getDriverNumber());
                    });

            InputProcessor.parseNumber(input.getRaceId(), Long.class)
                    .ifPresent(value -> {
                        GrandPrix grandPrix = grandPrixService.findById(value);
                        raceResult.setGrandPrix(grandPrix);
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
                .reader(raceResultReader())
                .processor(raceResultDataProcessor())
                .writer(itemWriterFactory.getItemWriter())
                .build();
    }
}
