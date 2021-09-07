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
import pl.salata.f1betapp.model.RaceResult;
import pl.salata.f1betapp.service.DriverService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

@AllArgsConstructor
@Service
public class RaceResultDataService {

    DriverService driverService;

    public List<RaceResult> populateRaceResult(Integer year, Integer round, Long id) throws IOException {
        List<RaceResultsResponse> fetched = fetchRaceResult(year, round);

        return processRaceResult(fetched, id);
    }

    private List<RaceResultsResponse> fetchRaceResult(Integer year, Integer round) throws IOException {
        String uri = "https://ergast.com/api/f1/" + year + "/" + round + "/results.json";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response;
        response = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), String.class);

        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        SimpleModule module = new SimpleModule();
        module.addDeserializer(RaceResultsResponse.class, new RaceResultsDeserializer());
        mapper.registerModule(module);

        JsonNode node = mapper.readTree(response.getBody())
                .path("MRData").path("RaceTable").path("Races").get(0).path("Results");
        JavaType customClassCollection = mapper.getTypeFactory()
                .constructCollectionType(List.class, RaceResultsResponse.class);

        return mapper.readerFor(customClassCollection).readValue(node);
    }

    private List<RaceResult> processRaceResult(List<RaceResultsResponse> resultsResponse, Long grandPrixId) {
        return resultsResponse.stream()
                .map(gp -> {
                    RaceResult raceResult = new RaceResult();
                    raceResult.setGrandPrixId(grandPrixId);
                    raceResult.setDriverName(gp.getDriverGivenName() + " " + gp.getDriverFamilyName());
                    raceResult.setDriverNumber(gp.getNumber());
                    raceResult.setTeamName(gp.getConstructorName());
                    try {
                        raceResult.setStartingGridPosition(Integer.parseInt(gp.getGrid()));
                    } catch (NumberFormatException e) {
                        System.out.println(e.getMessage());
                    }
                    raceResult.setFinishingPosition(gp.getPosition());
                    try {
                        raceResult.setPoints(Float.parseFloat(gp.getPoints()));
                    } catch (NumberFormatException e) {
                        System.out.println(e.getMessage());
                    }
                    raceResult.setLaps(gp.getLaps());
                    raceResult.setTime(gp.getTime());

                    try {
                        raceResult.setTimeInMilliseconds(Integer.parseInt(gp.getTimeInMilliseconds()));
                    } catch (NumberFormatException e) {
                        System.out.println(e.getMessage());
                    }
                    raceResult.setFastestLapTime(gp.getFastestLapTime());
                    raceResult.setFastestLapSpeed(gp.getFastestLapSpeed());
                    raceResult.setStatus(gp.getStatus());

                    String date = gp.getDriverDateOfBirth();
                    LocalDate dateOfBirth = Optional.of(LocalDate.parse(date, ISO_LOCAL_DATE)).get();

                    Driver driver = driverService.getByNameAndDateOfBirth(gp.getDriverGivenName(), gp.getDriverFamilyName(), dateOfBirth);
                    raceResult.setDriverId(driver.getId());

                    return raceResult;
                }).collect(Collectors.toList());
    }

}
