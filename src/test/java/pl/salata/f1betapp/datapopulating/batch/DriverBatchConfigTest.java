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
import pl.salata.f1betapp.datapopulating.batch.dto.DriverInput;
import pl.salata.f1betapp.model.Driver;
import pl.salata.f1betapp.model.RaceResult;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class DriverBatchConfigTest {

    @Mock
    private JobBuilderFactory jobBuilderFactory;
    @Mock
    private StepBuilderFactory stepBuilderFactory;
    @Mock
    private ItemWriterFactory<RaceResult> itemWriterFactory;
    @InjectMocks
    DriverBatchConfig driverBatchConfig;

    @Test
    void shouldReadDriverFromFile(@TempDir Path tempDir) throws Exception {
        //given
        final String FILE_NAME = "/driverTest.csv";
        generateTestCSV(tempDir + FILE_NAME);
        ExecutionContext executionContext = new ExecutionContext();
        JobParameters jobParameters = new JobParametersBuilder()
                .addParameter("dataSource", new JobParameter(tempDir + FILE_NAME))
                .toJobParameters();
        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution(jobParameters, executionContext);

        FlatFileItemReader<DriverInput> reader = driverBatchConfig.driverReader(tempDir + FILE_NAME);
        //when
        List<DriverInput> items = StepScopeTestUtils.doInStepScope(stepExecution, () -> {
            List<DriverInput> result = new ArrayList<>();
            DriverInput item;
            reader.open(stepExecution.getExecutionContext());
            while ((item = reader.read()) != null) {
                result.add(item);
            }
            reader.close();
            return result;
        });
        //then
        assertEquals(2, items.size());

        assertEquals("1", items.get(0).getDriverId());
        assertEquals("hamilton", items.get(0).getDriverRef());
        assertEquals("44", items.get(0).getNumber());
        assertEquals("HAM", items.get(0).getCode());
        assertEquals("Lewis", items.get(0).getForename());
        assertEquals("Hamilton", items.get(0).getSurname());
        assertEquals("1985-01-07", items.get(0).getDob());
        assertEquals("British", items.get(0).getNationality());
        assertEquals("http://en.wikipedia.org/wiki/Lewis_Hamilton", items.get(0).getUrl());

        assertEquals("heidfeld", items.get(1).getDriverRef());
    }

    private void generateTestCSV(String fileName) throws IOException {
        FileWriter writer = new FileWriter(fileName);
        writer.append("driverId,driverRef,number,code,forename,surname,dob,nationality,url\n");
        writer.append("1,\"hamilton\",44,\"HAM\",\"Lewis\",\"Hamilton\",\"1985-01-07\"," +
                "\"British\",\"http://en.wikipedia.org/wiki/Lewis_Hamilton\"\n");
        writer.append("2,\"heidfeld\",\\N,\"HEI\",\"Nick\",\"Heidfeld\",\"1977-05-10\"," +
                "\"German\",\"http://en.wikipedia.org/wiki/Nick_Heidfeld\"\n");
        writer.flush();
        writer.close();
    }

    @Test
    void shouldReturnProcessedDriver() throws Exception {
        //given
        DriverInput driverInput = new DriverInput();
        driverInput.setDriverId("2");
        driverInput.setDriverRef("driverRef");
        driverInput.setNumber("25");
        driverInput.setCode("PMA");
        driverInput.setForename("Forename");
        driverInput.setSurname("Surname");
        driverInput.setDob("1985-01-07");
        driverInput.setNationality("Nationality");
        driverInput.setUrl("http://url");

        ItemProcessor<DriverInput, Driver> processor = driverBatchConfig.driverDataProcessor();
        //when
        Driver driverProcessed = processor.process(driverInput);
        //then
        assertNotNull(driverProcessed);
        assertEquals(2L, driverProcessed.getId());
        assertEquals("25", driverProcessed.getDriverNumber());
        assertEquals("PMA", driverProcessed.getDriverCode());
        assertEquals("Forename", driverProcessed.getForename());
        assertEquals("Surname", driverProcessed.getSurname());
        assertEquals(LocalDate.of(1985, 1, 7), driverProcessed.getDateOfBirth());
        assertEquals("Nationality", driverProcessed.getNationality());
        assertEquals("http://url", driverProcessed.getUrl());
    }
}