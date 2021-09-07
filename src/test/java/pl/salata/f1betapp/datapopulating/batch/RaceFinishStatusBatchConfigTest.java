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
import pl.salata.f1betapp.datapopulating.batch.dto.RaceFinishStatusInput;
import pl.salata.f1betapp.model.RaceFinishStatus;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class RaceFinishStatusBatchConfigTest {

    @Mock
    public JobBuilderFactory jobBuilderFactory;
    @Mock
    public StepBuilderFactory stepBuilderFactory;
    @Mock
    public ItemWriterFactory<RaceFinishStatus> itemWriterFactory;

    @InjectMocks
    RaceFinishStatusBatchConfig raceFinishStatusBatchConfig;

    @Test
    void shouldReadRaceFinishStatusFromFile(@TempDir Path tempDir) throws Exception {
        //given
        final String FILE_NAME = "/statusTest.csv";
        generateTestCSV(tempDir + FILE_NAME);
        ExecutionContext executionContext = new ExecutionContext();
        JobParameters jobParameters = new JobParametersBuilder()
                .addParameter("dataSource", new JobParameter(tempDir + FILE_NAME))
                .toJobParameters();
        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution(jobParameters, executionContext);
        FlatFileItemReader<RaceFinishStatusInput> reader = raceFinishStatusBatchConfig.raceFinishStatusReader(tempDir + FILE_NAME);
        //when
        List<RaceFinishStatusInput> items = StepScopeTestUtils.doInStepScope(stepExecution, () -> {
            List<RaceFinishStatusInput> result = new ArrayList<>();
            RaceFinishStatusInput item;
            reader.open(stepExecution.getExecutionContext());
            while ((item = reader.read()) != null) {
                result.add(item);
            }
            reader.close();
            return result;
        });
        //then
        assertEquals(3, items.size());
        assertEquals("1", items.get(0).getStatusId());
        assertEquals("Finished", items.get(0).getStatus());
        assertEquals("Disqualified", items.get(1).getStatus());
        assertEquals("+4 Laps", items.get(2).getStatus());
    }

    @Test
    void shouldReturnProcessedStatus() throws Exception {
        //given
        ItemProcessor<RaceFinishStatusInput, RaceFinishStatus> processor = raceFinishStatusBatchConfig.raceFinishStatusDataProcessor();
        RaceFinishStatusInput statusInput = new RaceFinishStatusInput();
        statusInput.setStatusId("1");
        statusInput.setStatus("status1");
        //when
        RaceFinishStatus statusProcessed = processor.process(statusInput);
        //then
        assertNotNull(statusProcessed);
        assertEquals(1L, statusProcessed.getId());
        assertEquals("status1", statusProcessed.getStatus());
    }

    private void generateTestCSV(String fileName) throws IOException {
        FileWriter writer = new FileWriter(fileName);
        writer.append("statusId,status\n");
        writer.append("1,\"Finished\"\n");
        writer.append("2,\"Disqualified\"\n");
        writer.append("14,\"+4 Laps\"\n");
        writer.flush();
        writer.close();
    }
}