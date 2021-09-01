package pl.salata.f1betapp.datapopulating;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.StepScopeTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import pl.salata.f1betapp.model.Circuit;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@EnableAutoConfiguration
@SpringBatchTest
@ContextConfiguration(classes = {CircuitBatchConfig.class, ItemWriterFactory.class, JobCompletionNotificationListener.class})
class CircuitBatchConfigTest {

    @Autowired
    FlatFileItemReader<CircuitInput> reader;

    @Autowired
    ItemProcessor<CircuitInput, Circuit> processor;

    @Test
    void shouldReadCircuitFromFile(@TempDir Path tempDir) throws Exception {
        //given
        final String FILE_NAME = ".circuitTest.csv";
        generateTestCSV(tempDir + FILE_NAME);
        ExecutionContext executionContext = new ExecutionContext();
        JobParameters jobParameters = new JobParametersBuilder()
                .addParameter("dataSource", new JobParameter(tempDir + FILE_NAME))
                .toJobParameters();
        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution(jobParameters, executionContext);
        //when
        List<CircuitInput> items = StepScopeTestUtils.doInStepScope(stepExecution, () -> {
            List<CircuitInput> result = new ArrayList<>();
            CircuitInput item;
            reader.open(stepExecution.getExecutionContext());
            while ((item = reader.read()) != null) {
                result.add(item);
            }
            reader.close();
            return result;
        });
        //then
        assertEquals(3, items.size());

        assertEquals("1", items.get(0).getCircuitId());
        assertEquals("albert_park", items.get(0).getCircuitRef());
        assertEquals("Albert Park Grand Prix Circuit", items.get(0).getName());
        assertEquals("Melbourne", items.get(0).getLocation());
        assertEquals("Australia", items.get(0).getCountry());
        assertEquals("-37.8497", items.get(0).getLatitude());
        assertEquals("144.968", items.get(0).getLongitude());
        assertEquals("10", items.get(0).getAltitude());
        assertEquals("http://en.wikipedia.org/wiki/Melbourne_Grand_Prix_Circuit", items.get(0).getUrl());

        assertEquals("Sepang International Circuit", items.get(1).getName());
        assertEquals("Bahrain International Circuit", items.get(2).getName());
    }

    @Test
    void shouldReturnProcessedCircuit() throws Exception {
        //given
        CircuitInput circuitInput = new CircuitInput();
        circuitInput.setCircuitId("1");
        circuitInput.setCircuitRef("albert_park");
        circuitInput.setName("Albert Park");
        circuitInput.setLocation("Melbourne");
        circuitInput.setCountry("Australia");
        circuitInput.setLatitude("-37.8497");
        circuitInput.setLongitude("144.968");
        circuitInput.setAltitude("10");
        circuitInput.setUrl("http://en.wikipedia.org/wiki/Melbourne");
        //when
        Circuit circuitProcessed = processor.process(circuitInput);
        //then
        assertNotNull(circuitProcessed);
        assertEquals(1L, circuitProcessed.getId());
        assertEquals("Albert Park", circuitProcessed.getName());
        assertEquals("Melbourne", circuitProcessed.getLocation());
        assertEquals("Australia", circuitProcessed.getCountry());
        assertEquals("-37.8497", circuitProcessed.getLatitude());
        assertEquals("144.968", circuitProcessed.getLongitude());
        assertEquals("10", circuitProcessed.getAltitude());
        assertEquals("http://en.wikipedia.org/wiki/Melbourne", circuitInput.getUrl());
    }

    private void generateTestCSV(String fileName) {
        try {
            FileWriter writer = new FileWriter(fileName);
            writer.append("circuitId,circuitRef,name,location,country,lat,lng,alt,url\n");
            writer.append("1,\"albert_park\",\"Albert Park Grand Prix Circuit\",\"Melbourne\"," +
                    "\"Australia\",-37.8497,144.968,10,\"http://en.wikipedia.org/wiki/Melbourne_Grand_Prix_Circuit\"\n");
            writer.append("2,\"sepang\",\"Sepang International Circuit\",\"Kuala Lumpur\",\"Malaysia\"," +
                    "2.76083,101.738,18,\"http://en.wikipedia.org/wiki/Sepang_International_Circuit\"\n");
            writer.append("3,\"bahrain\",\"Bahrain International Circuit\",\"Sakhir\",\"Bahrain\"," +
                    "26.0325,50.5106,7,\"http://en.wikipedia.org/wiki/Bahrain_International_Circuit\"\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}