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
import pl.salata.f1betapp.datapopulating.batch.dto.QualificationResultInput;
import pl.salata.f1betapp.model.Driver;
import pl.salata.f1betapp.model.QualificationResult;
import pl.salata.f1betapp.model.RaceResult;
import pl.salata.f1betapp.model.Team;
import pl.salata.f1betapp.service.DriverService;
import pl.salata.f1betapp.service.TeamService;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QualificationResultBatchConfigTest {

    @Mock
    private JobBuilderFactory jobBuilderFactory;
    @Mock
    private StepBuilderFactory stepBuilderFactory;
    @Mock
    private ItemWriterFactory<RaceResult> itemWriterFactory;
    @Mock
    TeamService teamService;
    @Mock
    DriverService driverService;
    @InjectMocks
    QualificationResultBatchConfig qualificationResultBatchConfig;

    @Test
    void shouldReadQualificationResultsFromFile(@TempDir Path tempDir) throws Exception {
        //given
        final String FILE_NAME = "/qualificationResultTest.csv";
        generateTestCSV(tempDir + FILE_NAME);
        ExecutionContext executionContext = new ExecutionContext();
        JobParameters jobParameters = new JobParametersBuilder()
                .addParameter("dataSource", new JobParameter(tempDir + FILE_NAME))
                .toJobParameters();
        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution(jobParameters, executionContext);

        FlatFileItemReader<QualificationResultInput> reader = qualificationResultBatchConfig.qualificationResultReader(tempDir + FILE_NAME);
        //when
        List<QualificationResultInput> items = StepScopeTestUtils.doInStepScope(stepExecution, () -> {
            List<QualificationResultInput> result = new ArrayList<>();
            QualificationResultInput item;
            reader.open(stepExecution.getExecutionContext());
            while ((item = reader.read()) != null) {
                result.add(item);
            }
            reader.close();
            return result;
        });
        //then
        assertEquals(2, items.size());

        assertEquals("1", items.get(0).getQualifyId());
        assertEquals("18", items.get(0).getRaceId());
        assertEquals("1", items.get(0).getDriverId());
        assertEquals("1", items.get(0).getConstructorId());
        assertEquals("22", items.get(0).getNumber());
        assertEquals("1", items.get(0).getPosition());
        assertEquals("1:26.572", items.get(0).getQ1());
        assertEquals("1:25.187", items.get(0).getQ2());
        assertEquals("1:26.714", items.get(0).getQ3());

        assertEquals("4", items.get(1).getNumber());
    }

    private void generateTestCSV(String fileName) throws IOException {
        FileWriter writer = new FileWriter(fileName);
        writer.append("qualifyId,raceId,driverId,constructorId,number,position,q1,q2,q3\n");
        writer.append("1,18,1,1,22,1,\"1:26.572\",\"1:25.187\",\"1:26.714\"\n");
        writer.append("2,18,9,2,4,2,\"1:26.103\",\"1:25.315\",\"1:26.869\"\n");
        writer.flush();
        writer.close();
    }

    @Test
    void shouldReturnProcessedDriver() throws Exception {
        //given
        QualificationResultInput qualificationInput = createQualificationResultInput();

        Driver driver = new Driver();
        driver.setForename("Forename1");
        driver.setSurname("Surname2");
        when(driverService.getById(anyLong())).thenReturn(driver);

        Team team = new Team();
        team.setName("teamName1");
        when(teamService.getById(anyLong())).thenReturn(team);

        ItemProcessor<QualificationResultInput, QualificationResult> processor = qualificationResultBatchConfig.qualificationResultDataProcessor();
        //when
        QualificationResult qualificationProcessed = processor.process(qualificationInput);
        //then
        assertNotNull(qualificationProcessed);
        assertEquals(1L, qualificationProcessed.getId());
        assertEquals(18L, qualificationProcessed.getGrandPrixId());
        assertEquals("22", qualificationProcessed.getDriverNumber());
        assertEquals(5L, qualificationProcessed.getDriverId());
        assertEquals("Forename1 Surname2", qualificationProcessed.getDriverName());
        assertEquals("teamName1", qualificationProcessed.getTeamName());
        assertEquals(7, qualificationProcessed.getResult());
        assertEquals("1:25:111", qualificationProcessed.getQ1time());
        assertEquals("1:24:222", qualificationProcessed.getQ2time());
        assertEquals("1:23:333", qualificationProcessed.getQ3time());
    }

    @Test
    void shouldNotSetDriverNameNorTeamName() throws Exception {
        //given
        QualificationResultInput qualificationInput = createQualificationResultInput();
        qualificationInput.setDriverId(null);
        qualificationInput.setConstructorId(null);

        ItemProcessor<QualificationResultInput, QualificationResult> processor = qualificationResultBatchConfig.qualificationResultDataProcessor();
        //when
        QualificationResult qualificationProcessed = processor.process(qualificationInput);
        //then
        assertNotNull(qualificationProcessed);
        assertEquals(1L, qualificationProcessed.getId());
        assertNull(qualificationProcessed.getDriverName());
        assertNull(qualificationProcessed.getTeamName());
    }

    private QualificationResultInput createQualificationResultInput() {
        QualificationResultInput qualificationInput = new QualificationResultInput();
        qualificationInput.setQualifyId("1");
        qualificationInput.setRaceId("18");
        qualificationInput.setDriverId("5");
        qualificationInput.setConstructorId("2");
        qualificationInput.setNumber("22");
        qualificationInput.setPosition("7");
        qualificationInput.setQ1("1:25:111");
        qualificationInput.setQ2("1:24:222");
        qualificationInput.setQ3("1:23:333");
        return qualificationInput;
    }
}