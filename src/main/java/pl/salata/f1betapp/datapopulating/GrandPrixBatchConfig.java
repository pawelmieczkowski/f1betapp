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
import pl.salata.f1betapp.service.CircuitService;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

    @Bean
    public FlatFileItemReader<GrandPrixInput> grandPrixReader() {
        return new FlatFileItemReaderBuilder<GrandPrixInput>()
                .name("GrandPrixItemReader")
                .resource(new ClassPathResource("/data/races.csv")).delimited()
                .names(FIELD_NAMES)
                .linesToSkip(1)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<GrandPrixInput>() {{
                    setTargetType(GrandPrixInput.class);
                }})
                .build();
    }

    @Bean
    public GrandPrixDataProcessor grandPrixDataProcessor() {
        return new GrandPrixDataProcessor(circuitService);
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
