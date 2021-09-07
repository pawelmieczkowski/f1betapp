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
import pl.salata.f1betapp.datapopulating.batch.dto.GrandPrixInput;
import pl.salata.f1betapp.exception.EntityNotFoundException;
import pl.salata.f1betapp.model.Circuit;
import pl.salata.f1betapp.model.GrandPrix;
import pl.salata.f1betapp.model.QualificationResult;
import pl.salata.f1betapp.model.RaceResult;
import pl.salata.f1betapp.service.CircuitService;
import pl.salata.f1betapp.service.GrandPrixService;
import pl.salata.f1betapp.service.RaceResultService;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GrandPrixBatchConfigTest {

    @Mock
    private JobBuilderFactory jobBuilderFactory;
    @Mock
    private StepBuilderFactory stepBuilderFactory;
    @Mock
    private ItemWriterFactory<RaceResult> itemWriterFactory;
    @Mock
    private CircuitService circuitService;
    @Mock
    private RaceResultService raceResultService;
    @Mock
    private GrandPrixService grandPrixService;

    @InjectMocks
    GrandPrixBatchConfig grandPrixBatchConfig;


    @Test
    void shouldReadCGrandPrixFromFile(@TempDir Path tempDir) throws Exception {
        //given
        final String FILE_NAME = "/gradPrixTest.csv";
        generateTestCSV(tempDir + FILE_NAME);
        ExecutionContext executionContext = new ExecutionContext();
        JobParameters jobParameters = new JobParametersBuilder()
                .addParameter("dataSource", new JobParameter(tempDir + FILE_NAME))
                .toJobParameters();
        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution(jobParameters, executionContext);

        FlatFileItemReader<GrandPrixInput> reader = grandPrixBatchConfig.grandPrixReader(tempDir + FILE_NAME);
        //when
        List<GrandPrixInput> items = StepScopeTestUtils.doInStepScope(stepExecution, () -> {
            List<GrandPrixInput> result = new ArrayList<>();
            GrandPrixInput item;
            reader.open(stepExecution.getExecutionContext());
            while ((item = reader.read()) != null) {
                result.add(item);
            }
            reader.close();
            return result;
        });
        //then
        assertEquals(2, items.size());

        assertEquals("1", items.get(0).getRaceId());
        assertEquals("2009", items.get(0).getYear());
        assertEquals("1", items.get(0).getRound());
        assertEquals("1", items.get(0).getCircuitId());
        assertEquals("Australian Grand Prix", items.get(0).getName());
        assertEquals("2009-03-29", items.get(0).getDate());
        assertEquals("06:00:00", items.get(0).getTime());
        assertEquals("http://en.wikipedia.org/wiki/2009_Australian_Grand_Prix", items.get(0).getUrl());

        assertEquals("Malaysian Grand Prix", items.get(1).getName());
    }

    private void generateTestCSV(String fileName) throws IOException {
        FileWriter writer = new FileWriter(fileName);
        writer.append("raceId,year,round,circuitId,name,date,time,url\n");
        writer.append("1,2009,1,1,\"Australian Grand Prix\",\"2009-03-29\",\"06:00:00\",\"http://en.wikipedia.org/wiki/2009_Australian_Grand_Prix\"\n");
        writer.append("2,2009,2,2,\"Malaysian Grand Prix\",\"2009-04-05\",\"09:00:00\",\"http://en.wikipedia.org/wiki/2009_Malaysian_Grand_Prix\"\n");
        writer.flush();
        writer.close();
    }

    @Test
    void shouldReturnProcessedGrandPrix() throws Exception {
        //given
        GrandPrixInput grandPrixInput = createGrandPrixInput();
        Circuit circuit = new Circuit();
        circuit.setId(4L);
        circuit.setLocation("location1");
        circuit.setCountry("country2");
        when(raceResultService.getWinnerByGrandPrixId(1L)).thenReturn(Collections.singletonList("DriverName1"));
        when(circuitService.findById(4L)).thenReturn(circuit);

        GrandPrix mockedGrandPrix = new GrandPrix();
        mockedGrandPrix.setRaceResult(Arrays.asList(new RaceResult(), new RaceResult(), new RaceResult()));
        mockedGrandPrix.setQualificationResult(Arrays.asList(new QualificationResult(), new QualificationResult()));
        when(grandPrixService.getById(anyLong())).thenReturn(mockedGrandPrix);

        ItemProcessor<GrandPrixInput, GrandPrix> processor = grandPrixBatchConfig.grandPrixDataProcessor();
        //when
        GrandPrix grandPrixProcessed = processor.process(grandPrixInput);
        //then
        assertNotNull(grandPrixProcessed);
        assertEquals(1L, grandPrixProcessed.getId());
        assertEquals(2002, grandPrixProcessed.getYear());
        assertEquals(3, grandPrixProcessed.getRound());
        assertEquals(4L, grandPrixProcessed.getCircuit().getId());
        assertEquals("Name5", grandPrixProcessed.getName());
        assertEquals(LocalDate.of(2006, 3, 29), grandPrixProcessed.getDate());
        assertEquals(LocalTime.of(7, 0), grandPrixProcessed.getTime());
        assertEquals("url8", grandPrixProcessed.getUrl());
        assertEquals("DriverName1", grandPrixProcessed.getDriverName());
        assertEquals(3, grandPrixProcessed.getRaceResult().size());
        assertEquals(2, grandPrixProcessed.getQualificationResult().size());
    }

    @Test
    void shouldNotSetResultsNorDriver() throws Exception {
        //given
        GrandPrixInput grandPrixInput = createGrandPrixInput();
        when(circuitService.findById(anyLong())).thenThrow(new EntityNotFoundException(Circuit.class, "testMsgCircuit"));
        when(raceResultService.getWinnerByGrandPrixId(anyLong())).thenThrow(new EntityNotFoundException(String.class, "testMsgDriver"));
        when(grandPrixService.getById(anyLong())).thenThrow(new EntityNotFoundException(GrandPrix.class, "testMsgGrandPrix"));

        ItemProcessor<GrandPrixInput, GrandPrix> processor = grandPrixBatchConfig.grandPrixDataProcessor();
        //when
        GrandPrix grandPrixProcessed = processor.process(grandPrixInput);
        //then
        assertNotNull(grandPrixProcessed);
        assertNull(grandPrixProcessed.getRaceResult());
        assertNull(grandPrixProcessed.getQualificationResult());
        assertNull(grandPrixProcessed.getDriverName());
    }

    @Test
    void shouldSetMultipleDriverNames() throws Exception {
        //given
        GrandPrixInput grandPrixInput = createGrandPrixInput();
        when(raceResultService.getWinnerByGrandPrixId(1L)).thenReturn(Arrays.asList("DriverName1", "DriverName2"));
        when(circuitService.findById(4L)).thenReturn(new Circuit());

        GrandPrix mockedGrandPrix = new GrandPrix();
        mockedGrandPrix.setRaceResult(Collections.emptyList());
        mockedGrandPrix.setQualificationResult(Collections.emptyList());
        when(grandPrixService.getById(anyLong())).thenReturn(mockedGrandPrix);
        when(grandPrixService.getById(anyLong())).thenReturn(mockedGrandPrix);

        ItemProcessor<GrandPrixInput, GrandPrix> processor = grandPrixBatchConfig.grandPrixDataProcessor();
        //when
        GrandPrix grandPrixProcessed = processor.process(grandPrixInput);
        //then
        assertNotNull(grandPrixProcessed);
        assertEquals("DriverName1, DriverName2", grandPrixProcessed.getDriverName());
    }

    private GrandPrixInput createGrandPrixInput() {
        GrandPrixInput grandPrixInput = new GrandPrixInput();
        grandPrixInput.setRaceId("1");
        grandPrixInput.setYear("2002");
        grandPrixInput.setRound("3");
        grandPrixInput.setCircuitId("4");
        grandPrixInput.setName("Name5");
        grandPrixInput.setDate("2006-03-29");
        grandPrixInput.setTime("07:00:00");
        grandPrixInput.setUrl("url8");
        return grandPrixInput;
    }
}