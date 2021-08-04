package pl.salata.f1betapp.datapopulating;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
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
import pl.salata.f1betapp.model.QualificationResult;
import pl.salata.f1betapp.model.RaceResult;
import pl.salata.f1betapp.service.CircuitService;
import pl.salata.f1betapp.service.GrandPrixService;
import pl.salata.f1betapp.service.RaceResultService;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class GrandPrixBatchConfig {

    private final String[] FIELD_NAMES = new String[]{
            "raceId", "year", "round", "circuitId", "name", "date", "time", "url"
    };

    public JobBuilderFactory jobBuilderFactory;
    public StepBuilderFactory stepBuilderFactory;

    public ItemWriterFactory<GrandPrix> itemWriterFactory;

    private final CircuitService circuitService;
    private final RaceResultService raceResultService;
    private final GrandPrixService grandPrixService;

    @Bean
    public FlatFileItemReader<GrandPrixInput> grandPrixReader() {
        return new FlatFileItemReaderBuilder<GrandPrixInput>()
                .name("GrandPrixItemReader")
                .resource(new ClassPathResource("/data/races.csv")).delimited()
                .names(FIELD_NAMES)
                .linesToSkip(1)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(GrandPrixInput.class);
                }})
                .build();
    }

    @Bean
    public ItemProcessor<GrandPrixInput, GrandPrix> grandPrixDataProcessor() {
        return input -> {
            GrandPrix grandPrix = new GrandPrix();
            InputProcessor.parseNumber(input.getRaceId(), Long.class)
                    .ifPresent(value -> {
                        grandPrix.setId(value);

                        List<String> winners = raceResultService.getWinnerByGrandPrixId(value);
                        if(winners.size() > 0) {
                            String driverName = winners.get(0);
                            //there are a few gp in history when there where 2 winners:
                            for (int i = 1; i < winners.size(); i++) {
                                driverName += ", " + winners.get(i);
                            }
                            grandPrix.setDriverName(driverName);
                        }

                        List<RaceResult> raceResults = grandPrixService.getById(value).getRaceResult();
                        grandPrix.setRaceResult(raceResults);

                        List<QualificationResult> qualificationResults = grandPrixService.getById(value).getQualificationResult();
                        grandPrix.setQualificationResult(qualificationResults);
                    });
            InputProcessor.parseNumber(input.getRound(), Integer.class).ifPresent(grandPrix::setRound);
            InputProcessor.parseNumber(input.getYear(), Integer.class).ifPresent(grandPrix::setYear);
            grandPrix.setName(InputProcessor.validateString(input.getName()));
            grandPrix.setUrl(InputProcessor.validateString(input.getUrl()));

            InputProcessor.parseNumber(input.getCircuitId(), Long.class)
                    .ifPresent(value -> {
                        Circuit circuit = circuitService.findById(value);
                        grandPrix.setCircuit(circuit);
                        grandPrix.setLocalization(circuit.getLocation());
                        grandPrix.setCountry(circuit.getCountry());
                    });
            grandPrix.setDate(InputProcessor.parseDate(input.getDate()));
            grandPrix.setTime(InputProcessor.parseTime(input.getTime()));
            return grandPrix;
        };
    }


    @Bean
    public Job importGrandPrixJob(JobCompletionNotificationListener listener, Step stepGrandPrix) {
        return jobBuilderFactory.get("importGrandPrixJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(stepGrandPrix)
                .end()
                .build();
    }


    @Bean
    public Step stepGrandPrix() {
        return stepBuilderFactory.get("stepGrandPrix")
                .<GrandPrixInput, GrandPrix>chunk(10)
                .reader(grandPrixReader())
                .processor(grandPrixDataProcessor())
                .writer(itemWriterFactory.getItemWriter())
                .build();
    }
}
