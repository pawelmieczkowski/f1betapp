package pl.salata.f1betapp.datapopulating.batch;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.StepScopeTestUtils;
import pl.salata.f1betapp.datapopulating.batch.dto.TeamInput;
import pl.salata.f1betapp.model.Team;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class TeamBatchConfigTest {

    @Mock
    public JobBuilderFactory jobBuilderFactory;
    @Mock
    public StepBuilderFactory stepBuilderFactory;
    @Mock
    public ItemWriterFactory<Team> itemWriterFactory;

    @InjectMocks
    TeamBatchConfig teamBatchConfig;

    @Test
    void shouldReadTeamFromFile(@TempDir Path tempDir) throws Exception {
        //given
        final String FILE_NAME = "/teamTest.csv";
        generateTestCSV(tempDir + FILE_NAME);
        ExecutionContext executionContext = new ExecutionContext();
        JobParameters jobParameters = new JobParametersBuilder()
                .addParameter("dataSource", new JobParameter(tempDir + FILE_NAME))
                .toJobParameters();
        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution(jobParameters, executionContext);
        FlatFileItemReader<TeamInput> reader = teamBatchConfig.teamReader(tempDir + FILE_NAME);
        //when
        List<TeamInput> items = StepScopeTestUtils.doInStepScope(stepExecution, () -> {
            List<TeamInput> result = new ArrayList<>();
            TeamInput item;
            reader.open(stepExecution.getExecutionContext());
            while ((item = reader.read()) != null) {
                result.add(item);
            }
            reader.close();
            return result;
        });
        //then
        assertEquals(2, items.size());
        assertEquals("1", items.get(0).getConstructorId());
        assertEquals("mclaren", items.get(0).getConstructorRef());
        assertEquals("McLaren", items.get(0).getName());
        assertEquals("British", items.get(0).getNationality());
        assertEquals("http://en.wikipedia.org/wiki/McLaren", items.get(0).getUrl());
        assertEquals("German", items.get(1).getNationality());
    }

    @Test
    void shouldReturnProcessedTeam() throws Exception {
        //given
        ItemProcessor<TeamInput, Team> processor = teamBatchConfig.teamDataProcessor();
        TeamInput teamInput = new TeamInput();
        teamInput.setConstructorId("2");
        teamInput.setConstructorRef("testRef");
        teamInput.setName("testName");
        teamInput.setNationality("testNationality");
        teamInput.setUrl("testUrl");
        //when
        Team teamProcessed = processor.process(teamInput);
        //then
        assertNotNull(teamProcessed);
        assertEquals(2L, teamProcessed.getId());
        assertEquals("testName", teamProcessed.getName());
        assertEquals("testNationality", teamProcessed.getNationality());
        assertEquals("testUrl", teamProcessed.getUrl());
    }

    private void generateTestCSV(String fileName) throws IOException {
        FileWriter writer = new FileWriter(fileName);
        writer.append("constructorId,constructorRef,name,nationality,url\n");
        writer.append("1,\"mclaren\",\"McLaren\",\"British\",\"http://en.wikipedia.org/wiki/McLaren\"\n");
        writer.append("2,\"bmw_sauber\",\"BMW Sauber\",\"German\",\"http://en.wikipedia.org/wiki/BMW_Sauber\"\n");
        writer.flush();
        writer.close();
    }
}