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
import pl.salata.f1betapp.model.Circuit;

import javax.persistence.EntityManagerFactory;


@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class CircuitBatchConfig {

    private final String[] FIELD_NAMES = new String[]{
            "circuitId", "circuitRef", "name", "location", "country", "latitude", "longitude", "altitude", "url"
    };


    public JobBuilderFactory jobBuilderFactory;
    public StepBuilderFactory stepBuilderFactory;

    public ItemWriterFactory<Circuit> itemWriterFactory;

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
    public ItemProcessor<CircuitInput, Circuit> circuitProcessor() {
        return input -> {
            Circuit circuit = new Circuit();

            InputProcessor.parseNumber(input.getCircuitId(), Long.class).ifPresent(circuit::setId);

            circuit.setName(InputProcessor.validateString(input.getName()));
            circuit.setLocation(InputProcessor.validateString(input.getLocation()));
            circuit.setCountry(InputProcessor.validateString(input.getCountry()));
            circuit.setUrl(InputProcessor.validateString(input.getUrl()));

            circuit.setLatitude(InputProcessor.validateString(input.getLatitude()));
            circuit.setLongitude(InputProcessor.validateString(input.getLongitude()));
            circuit.setAltitude(InputProcessor.validateString(input.getAltitude()));

            return circuit;
        };
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
    public Step stepCircuit() {
        return stepBuilderFactory.get("stepCircuit")
                .<CircuitInput, Circuit>chunk(10)
                .reader(circuitReader())
                .processor(circuitProcessor())
                .writer(itemWriterFactory.getItemWriter())
                .build();
    }


}
