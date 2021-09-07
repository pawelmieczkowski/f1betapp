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
import pl.salata.f1betapp.datapopulating.batch.dto.TeamInput;
import pl.salata.f1betapp.model.Team;

@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class TeamBatchConfig {

    private final String[] FIELD_NAMES = new String[]{
            "constructorId", "constructorRef", "name", "nationality", "url"
    };

    public JobBuilderFactory jobBuilderFactory;

    public StepBuilderFactory stepBuilderFactory;

    public ItemWriterFactory<Team> itemWriterFactory;

    @Bean
    @StepScope
    public FlatFileItemReader<TeamInput> teamReader(@Value("#{jobParameters['dataSource']}") String dataPath) {
        return new FlatFileItemReaderBuilder<TeamInput>()
                .name("teamReader")
                .resource(new PathResource(dataPath)).delimited()
                .names(FIELD_NAMES)
                .linesToSkip(1)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(TeamInput.class);
                }})
                .build();
    }

    @Bean
    public ItemProcessor<TeamInput, Team> teamDataProcessor() {
        return input -> {
            Team team = new Team();

            InputProcessor.parseNumber(input.getConstructorId(), Long.class).ifPresent(team::setId);

            InputProcessor.validateString(input.getName()).ifPresent(team::setName);
            InputProcessor.validateString(input.getNationality()).ifPresent(team::setNationality);
            InputProcessor.validateString(input.getUrl()).ifPresent(team::setUrl);

            return team;
        };
    }

    @Bean
    public Job importTeamJob(JobCompletionNotificationListener listener, Step stepTeam) {
        return jobBuilderFactory.get("importTeamJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(stepTeam)
                .end()
                .build();
    }

    @Bean
    public Step stepTeam() {
        return stepBuilderFactory.get("stepDriver")
                .<TeamInput, Team>chunk(10)
                .reader(teamReader("OVERRIDDEN_BY_EXPRESSION"))
                .processor(teamDataProcessor())
                .writer(itemWriterFactory.getItemWriter())
                .build();
    }
}
