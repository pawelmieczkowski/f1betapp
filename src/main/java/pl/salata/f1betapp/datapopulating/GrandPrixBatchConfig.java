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
import pl.salata.f1betapp.exception.EntityNotFoundException;
import pl.salata.f1betapp.model.Circuit;
import pl.salata.f1betapp.model.GrandPrix;
import pl.salata.f1betapp.model.QualificationResult;
import pl.salata.f1betapp.model.RaceResult;
import pl.salata.f1betapp.service.CircuitService;
import pl.salata.f1betapp.service.GrandPrixService;
import pl.salata.f1betapp.service.RaceResultService;

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
    @StepScope
    public FlatFileItemReader<GrandPrixInput> grandPrixReader(@Value("#{jobParameters['dataSource']}") String dataPath) {
        return new FlatFileItemReaderBuilder<GrandPrixInput>()
                .name("GrandPrixItemReader")
                .resource(new PathResource(dataPath)).delimited()
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
                        try {
                            List<String> winners = raceResultService.getWinnerByGrandPrixId(value);
                            if (winners.size() > 0) {
                                String driverName = winners.get(0);
                                //there are a few gp in history when there were 2 winners:
                                for (int i = 1; i < winners.size(); i++) {
                                    driverName += ", " + winners.get(i);
                                }
                                grandPrix.setDriverName(driverName);
                            }
                        } catch (EntityNotFoundException e) {
                            System.out.println(e.getMessage());
                        }
                        try {
                            GrandPrix grandPrixWithResults = grandPrixService.getById(value);

                            List<RaceResult> raceResults = grandPrixWithResults.getRaceResult();
                            grandPrix.setRaceResult(raceResults);

                            List<QualificationResult> qualificationResults = grandPrixWithResults.getQualificationResult();
                            grandPrix.setQualificationResult(qualificationResults);
                        } catch (EntityNotFoundException e) {
                            System.out.println(e.getMessage());
                        }
                    });
            InputProcessor.parseNumber(input.getRound(), Integer.class).ifPresent(grandPrix::setRound);
            InputProcessor.parseNumber(input.getYear(), Integer.class).ifPresent(grandPrix::setYear);
            grandPrix.setName(InputProcessor.validateString(input.getName()));
            grandPrix.setUrl(InputProcessor.validateString(input.getUrl()));

            InputProcessor.parseNumber(input.getCircuitId(), Long.class)
                    .ifPresent(value -> {
                        try {
                            Circuit circuit = circuitService.findById(value);
                            grandPrix.setCircuit(circuit);
                            grandPrix.setLocalization(circuit.getLocation());
                            grandPrix.setCountry(circuit.getCountry());
                        } catch (EntityNotFoundException e) {
                            System.out.println(e.getMessage());
                        }
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
                .reader(grandPrixReader("OVERRIDDEN_BY_EXPRESSION"))
                .processor(grandPrixDataProcessor())
                .writer(itemWriterFactory.getItemWriter())
                .build();
    }
}
