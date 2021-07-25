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
import pl.salata.f1betapp.model.RaceFinishStatus;
import pl.salata.f1betapp.model.RaceResult;
import pl.salata.f1betapp.service.DriverService;
import pl.salata.f1betapp.service.GrandPrixService;
import pl.salata.f1betapp.service.RaceFinishStatusService;
import pl.salata.f1betapp.service.TeamService;

import javax.sql.DataSource;

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
    public RaceResultDataProcessor raceResultDataProcessor() {
        return new RaceResultDataProcessor(statusService, teamService, driverService, grandPrixService);
    }

    @Bean
    public JdbcBatchItemWriter<RaceResult> raceResultWriter(DataSource dataSource) {

        return new JdbcBatchItemWriterBuilder<RaceResult>()
                .itemPreparedStatementSetter((item, ps) -> {
                    ps.setLong(1, item.getId());
                    ps.setLong(2, item.getGrandPrix().getId());
                    ps.setString(3, item.getDriverNumber());
                    ps.setString(4, item.getDriverName());
                    ps.setString(5, item.getTeamName());
                    ps.setObject(6, item.getStartingGridPosition());
                    ps.setString(7, item.getFinishingPosition());
                    ps.setObject(8, item.getPoints());
                    ps.setString(9, item.getLaps());
                    ps.setString(10, item.getTime());
                    ps.setObject(11, item.getTimeInMilliseconds());
                    ps.setString(12, item.getFastestLapTime());
                    ps.setString(13, item.getFastestLapSpeed());
                    ps.setString(14, item.getStatus());
                    ps.setObject(15, item.getRanking());
                })
                .sql("INSERT INTO race_result (id, grand_prix_Id, driver_number, driver_name, team_name, " +
                        "starting_grid_position, finishing_position, points, laps, time, time_in_milliseconds, fastest_lap_time, " +
                        "fastest_lap_speed, status, ranking) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")
                .dataSource(dataSource)
                .build();
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
    public Step stepRaceResult(JdbcBatchItemWriter<RaceResult> raceResultWriter) {
        return stepBuilderFactory.get("stepRaceResult")
                .<RaceResultInput, RaceResult>chunk(10)
                .reader(raceResultReader())
                .processor(raceResultDataProcessor())
                .writer(raceResultWriter)
                .build();
    }
}
