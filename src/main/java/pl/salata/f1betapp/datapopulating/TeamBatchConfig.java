package pl.salata.f1betapp.datapopulating;

import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import pl.salata.f1betapp.model.Driver;
import pl.salata.f1betapp.model.Team;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class TeamBatchConfig {

    private final String[] FIELD_NAMES = new String[]{
            "constructorId", "constructorRef", "name", "nationality", "url"
    };

    public JobBuilderFactory jobBuilderFactory;

    public StepBuilderFactory stepBuilderFactory;

    @Bean
    public FlatFileItemReader<TeamInput> teamReader() {
        return new FlatFileItemReaderBuilder<TeamInput>()
                .name("teamReader")
                .resource(new ClassPathResource("/data/constructors.csv")).delimited()
                .names(FIELD_NAMES)
                .linesToSkip(1)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<TeamInput>() {{
                    setTargetType(TeamInput.class);
                }})
                .build();
    }

    @Bean
    public ItemProcessor<TeamInput, Team> teamDataProcessor() {
        return input -> {
            Team team = new Team();

            InputProcessor.parseNumber(input.getConstructorId(), Long.class).ifPresent(team::setId);

            team.setName(InputProcessor.validateString(input.getName()));
            team.setNationality(InputProcessor.validateString(input.getNationality()));
            team.setUrl(InputProcessor.validateString(input.getUrl()));

            return team;
        };
    }

    @Bean
    public JdbcBatchItemWriter<Team> teamWriter(DataSource dataSource) {

        return new JdbcBatchItemWriterBuilder<Team>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO team (id, name, nationality, url) " +
                        "VALUES (:id, :name, :nationality, :url)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Job importTeamJob(JobCompletionNotificationListener listener, Step setTeam) {
        return jobBuilderFactory.get("importTeamJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(setTeam)
                .end()
                .build();
    }


    @Bean
    public Step setTeam(JdbcBatchItemWriter<Team> teamWriter) {
        return stepBuilderFactory.get("stepDriver")
                .<TeamInput, Team>chunk(10)
                .reader(teamReader())
                .processor(teamDataProcessor())
                .writer(teamWriter)
                .build();
    }
}
