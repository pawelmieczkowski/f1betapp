package pl.salata.f1betapp.datapopulating.api;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.salata.f1betapp.model.Driver;
import pl.salata.f1betapp.model.QualificationResult;
import pl.salata.f1betapp.service.DriverService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

@AllArgsConstructor
@Service
public class QualifyingResultDataService {

    DriverService driverService;

    public List<QualificationResult> populateRaceResult(Integer year, Integer round, Long id) throws IOException {
        List<QualifyingResultResponse> fetched = fetchQualifyingResult(year, round);

        return processRaceResult(fetched, id);
    }

    private List<QualifyingResultResponse> fetchQualifyingResult(Integer year, Integer round) throws IOException {
        String uri = "https://ergast.com/api/f1/" + year + "/" + round + "/qualifying.json";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response;
        response = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), String.class);

        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        SimpleModule module = new SimpleModule();
        module.addDeserializer(QualifyingResultResponse.class, new QualifyingResultDeserializer());
        mapper.registerModule(module);

        JsonNode node = mapper.readTree(response.getBody()).at("/MRData/RaceTable/Races/0/QualifyingResults");
        JavaType customClassCollection = mapper.getTypeFactory()
                .constructCollectionType(List.class, QualifyingResultResponse.class);

        return mapper.readerFor(customClassCollection).readValue(node);
    }

    private List<QualificationResult> processRaceResult(List<QualifyingResultResponse> response, Long grandPrixId) {
        return response.stream()
                .map(gp -> {
                    QualificationResult qualificationResult = new QualificationResult();

                    qualificationResult.setGrandPrixId(grandPrixId);
                    qualificationResult.setDriverNumber(gp.getNumber());
                    qualificationResult.setDriverName(gp.getDriverGivenName() + " " + gp.getDriverFamilyName());
                    qualificationResult.setTeamName(gp.getConstructorName());
                    qualificationResult.setResult(Integer.parseInt(gp.getPosition()));
                    qualificationResult.setQ1time(gp.getQ1time());
                    qualificationResult.setQ2time(gp.getQ2time());
                    qualificationResult.setQ3time(gp.getQ3time());

                    String date = gp.getDriverDateOfBirth();
                    LocalDate dateOfBirth = Optional.of(LocalDate.parse(date, ISO_LOCAL_DATE)).get();

                    Driver driver = driverService.getByNameAndDateOfBirth(gp.getDriverGivenName(), gp.getDriverFamilyName(), dateOfBirth);
                    qualificationResult.setDriverId(driver.getId());

                    return qualificationResult;
                }).collect(Collectors.toList());
    }

}
