package pl.salata.f1betapp.datapopulating;

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
import pl.salata.f1betapp.model.Driver;
import pl.salata.f1betapp.model.RaceFinishStatus;
import pl.salata.f1betapp.model.RaceResult;
import pl.salata.f1betapp.model.Team;
import pl.salata.f1betapp.service.DriverService;
import pl.salata.f1betapp.service.RaceFinishStatusService;
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
class RaceResultBatchConfigTest {

    @Mock
    private JobBuilderFactory jobBuilderFactory;
    @Mock
    private StepBuilderFactory stepBuilderFactory;
    @Mock
    private ItemWriterFactory<RaceResult> itemWriterFactory;
    @Mock
    private RaceFinishStatusService statusService;
    @Mock
    private TeamService teamService;
    @Mock
    private DriverService driverService;

    @InjectMocks
    RaceResultBatchConfig raceResultBatchConfig;

    @Test
    void shouldReadRaceResultsFromFile(@TempDir Path tempDir) throws Exception {
        //given
        final String FILE_NAME = "/qualificationResultTest.csv";
        generateTestCSV(tempDir + FILE_NAME);
        ExecutionContext executionContext = new ExecutionContext();
        JobParameters jobParameters = new JobParametersBuilder()
                .addParameter("dataSource", new JobParameter(tempDir + FILE_NAME))
                .toJobParameters();
        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution(jobParameters, executionContext);
        FlatFileItemReader<RaceResultInput> reader = raceResultBatchConfig.raceResultReader(tempDir + FILE_NAME);
        //when
        List<RaceResultInput> items = StepScopeTestUtils.doInStepScope(stepExecution, () -> {
            List<RaceResultInput> result = new ArrayList<>();
            RaceResultInput item;
            reader.open(stepExecution.getExecutionContext());
            while ((item = reader.read()) != null) {
                result.add(item);
            }
            reader.close();
            return result;
        });
        //then
        assertEquals(2, items.size());

        assertEquals("1", items.get(0).getResultId());
        assertEquals("18", items.get(0).getRaceId());
        assertEquals("5", items.get(0).getDriverId());
        assertEquals("6", items.get(0).getConstructorId());
        assertEquals("22", items.get(0).getNumber());
        assertEquals("7", items.get(0).getGrid());
        assertEquals("8", items.get(0).getPosition());
        assertEquals("9", items.get(0).getPositionText());
        assertEquals("3", items.get(0).getPositionOrder());
        assertEquals("10", items.get(0).getPoints());
        assertEquals("58", items.get(0).getLaps());
        assertEquals("1:34:50.616", items.get(0).getTime());
        assertEquals("5690616", items.get(0).getMilliseconds());
        assertEquals("39", items.get(0).getFastestLap());
        assertEquals("2", items.get(0).getRank());
        assertEquals("1:27.452", items.get(0).getFastestLapTime());
        assertEquals("218.300", items.get(0).getFastestLapSpeed());
        assertEquals("15", items.get(0).getStatusId());

        assertEquals("2", items.get(1).getResultId());
    }

    private void generateTestCSV(String fileName) throws IOException {
        FileWriter writer = new FileWriter(fileName);
        writer.append("resultId,raceId,driverId,constructorId,number,grid,position,positionText,positionOrder," +
                "points,laps,time,milliseconds,fastestLap,rank,fastestLapTime,fastestLapSpeed,statusId\n");
        writer.append("1,18,5,6,22,7,8,\"9\",3,10,58,\"1:34:50.616\",5690616,39,2,\"1:27.452\",\"218.300\",15\n");
        writer.append("2,28,25,22,23,25,22,\"22\",22,28,58,\"+5.478\",5696094,41,3,\"1:27.739\",\"217.586\",16\n");
        writer.flush();
        writer.close();
    }

    @Test
    void shouldReturnProcessedResults() throws Exception {
        //given
        RaceResultInput raceResultInput = createRaceResultInput();
        ItemProcessor<RaceResultInput, RaceResult> processor = raceResultBatchConfig.raceResultDataProcessor();

        RaceFinishStatus raceFinishStatus = new RaceFinishStatus();
        raceFinishStatus.setStatus("status1");
        when(statusService.findById(anyLong())).thenReturn(raceFinishStatus);

        Team team = new Team();
        team.setName("teamName1");
        when(teamService.getById(anyLong())).thenReturn(team);

        Driver driver = new Driver();
        driver.setForename("Forename1");
        driver.setSurname("Surname1");
        driver.setDriverNumber("driverNumber1");
        when(driverService.getById(anyLong())).thenReturn(driver);
        //when
        RaceResult resultProcessed = processor.process(raceResultInput);
        //then
        assertNotNull(resultProcessed);
        assertEquals(1L, resultProcessed.getId());
        assertEquals(2L, resultProcessed.getGrandPrixId());
        assertEquals(3L, resultProcessed.getDriverId());
        assertEquals("driverNumber1", resultProcessed.getDriverNumber());
        assertEquals("Forename1 Surname1", resultProcessed.getDriverName());
        assertEquals("teamName1", resultProcessed.getTeamName());
        assertEquals(6, resultProcessed.getStartingGridPosition());
        assertEquals("8", resultProcessed.getFinishingPosition());
        assertEquals(7, resultProcessed.getPoints());
        assertEquals("11", resultProcessed.getLaps());
        assertEquals("1:34:50.616", resultProcessed.getTime());
        assertEquals(5690616, resultProcessed.getTimeInMilliseconds());
        assertEquals("1:27.452", resultProcessed.getFastestLapTime());
        assertEquals("26", resultProcessed.getFastestLapSpeed());
        assertEquals("status1", resultProcessed.getStatus());
        assertEquals(27, resultProcessed.getRanking());
    }

    @Test
    void shouldNotSetStatusNorTeamNameNorDriverNameNorDriverNumber() throws Exception {
        //given
        ItemProcessor<RaceResultInput, RaceResult> processor = raceResultBatchConfig.raceResultDataProcessor();
        RaceResultInput raceResultInput = createRaceResultInput();
        raceResultInput.setStatusId(null);
        raceResultInput.setConstructorId(null);
        raceResultInput.setDriverId(null);
        //when
        RaceResult resultProcessed = processor.process(raceResultInput);
        //then
        assertNotNull(resultProcessed);
        assertNull(resultProcessed.getStatus());
        assertNull(resultProcessed.getTeamName());
        assertNull(resultProcessed.getDriverName());
        assertNull(resultProcessed.getDriverNumber());
    }



    private RaceResultInput createRaceResultInput() {
        RaceResultInput raceResultInput = new RaceResultInput();
        raceResultInput.setResultId("1");
        raceResultInput.setRaceId("2");
        raceResultInput.setDriverId("3");
        raceResultInput.setConstructorId("4");
        raceResultInput.setNumber("55");
        raceResultInput.setGrid("6");
        raceResultInput.setPoints("7");
        raceResultInput.setPositionText("8");
        raceResultInput.setPositionOrder("9");
        raceResultInput.setLaps("11");
        raceResultInput.setTime("1:34:50.616");
        raceResultInput.setMilliseconds("5690616");
        raceResultInput.setFastestLap("25");
        raceResultInput.setRank("27");
        raceResultInput.setFastestLapTime("1:27.452");
        raceResultInput.setFastestLapSpeed("26");
        raceResultInput.setStatusId("12");
        return raceResultInput;
    }
}