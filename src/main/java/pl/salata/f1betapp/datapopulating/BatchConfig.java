package pl.salata.f1betapp.datapopulating;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import pl.salata.f1betapp.model.Circuit;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class BatchConfig {

    private final String[] FIELD_NAMES = new String[]{
            "circuitId", "circuitRef", "name", "location", "country", "latitude", "longitude", "altitude", "url"
    };


    public JobBuilderFactory jobBuilderFactory;

    public StepBuilderFactory stepBuilderFactory;

    private final JobRepository jobRepository;

    @Bean
    public FlatFileItemReader<CircuitInput> circuitReader() {
        return new FlatFileItemReaderBuilder<CircuitInput>()
                .name("CircuitItemReader")
                .resource(new ClassPathResource("/data/circuits.csv")).delimited()
                .names(FIELD_NAMES)
                .linesToSkip(1)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<CircuitInput>() {{
                    setTargetType(CircuitInput.class);
                }})
                .build();
    }

    @Bean
    public CircuitDataProcessor circuitProcessor() {
        return new CircuitDataProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Circuit> circuitWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Circuit>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO circuit (id, name, location, country, latitude, longitude, altitude, url) " +
                        "VALUES (:id, :name, :location, :country, :latitude, :longitude, :altitude, :url)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Job importCircuitJob(JobCompletionNotificationListener listener, Step stepCircuit) {
        return jobBuilderFactory.get("importCircuitJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(stepCircuit)
                .end()
                .build();
    }

    @Bean
    public Step stepCircuit(JdbcBatchItemWriter<Circuit> circuitWriter) {
        return stepBuilderFactory.get("stepCircuit")
                .<CircuitInput, Circuit>chunk(10)
                .reader(circuitReader())
                .processor(circuitProcessor())
                .writer(circuitWriter)
                .build();
    }

    @Bean(name = "jobCustomLauncher")
    public JobLauncher jobLauncher() throws Exception{
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }
}
