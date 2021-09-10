package pl.salata.f1betapp.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.salata.f1betapp.model.Circuit;
import pl.salata.f1betapp.model.GrandPrix;
import pl.salata.f1betapp.model.QualificationResult;
import pl.salata.f1betapp.model.RaceResult;
import pl.salata.f1betapp.service.RaceResultService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RaceResultControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    RaceResultService raceResultService;

    @Test
    void shouldReturnRaceResultsWithoutRaceResult() throws Exception {
        //given
        List<RaceResult> results = generateResults();
        when(raceResultService.getByGrandPrixId(anyLong())).thenReturn(results);
        //then
        mockMvc.perform(get("/api/race-result")
                .param("grandPrix", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].driverId", is(11)))
                .andExpect(jsonPath("$[0].driverNumber", is("driverNumber1")))
                .andExpect(jsonPath("$[0].driverName", is("driverName1")))
                .andExpect(jsonPath("$[0].teamName", is("teamName1")))
                .andExpect(jsonPath("$[0].startingGridPosition", is(11)))
                .andExpect(jsonPath("$[0].finishingPosition", is("finishingPos1")))
                .andExpect(jsonPath("$[0].points", is(11.1)))
                .andExpect(jsonPath("$[0].laps", is("laps1")))
                .andExpect(jsonPath("$[0].time", is("time1")))
                .andExpect(jsonPath("$[0].timeInMilliseconds", is(123456789)))
                .andExpect(jsonPath("$[0].fastestLapTime", is("lapTime1")))
                .andExpect(jsonPath("$[0].fastestLapSpeed", is("lapSpeed1")))
                .andExpect(jsonPath("$[0].status", is("status1")))
                .andExpect(jsonPath("$[0].ranking", is(1)))
                .andExpect(jsonPath("$[0].grandPrix").doesNotExist())

                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].driverName", is("driverName2")))
                .andExpect(jsonPath("$[1].grandPrix").doesNotExist());
    }

    @Test
    void shouldReturnDriversRaceResultsWithGrandPrixWithoutResultsAndCircuit() throws Exception {
        //given
        List<RaceResult> results = generateResults();
        when(raceResultService.getDriverResults(anyLong())).thenReturn(results);

        //then
        mockMvc.perform(get("/api/race-result/driver")
                .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].driverId", is(11)))
                .andExpect(jsonPath("$[0].driverNumber", is("driverNumber1")))
                .andExpect(jsonPath("$[0].driverName", is("driverName1")))
                .andExpect(jsonPath("$[0].teamName", is("teamName1")))
                .andExpect(jsonPath("$[0].startingGridPosition", is(11)))
                .andExpect(jsonPath("$[0].finishingPosition", is("finishingPos1")))
                .andExpect(jsonPath("$[0].points", is(11.1)))
                .andExpect(jsonPath("$[0].laps", is("laps1")))
                .andExpect(jsonPath("$[0].time", is("time1")))
                .andExpect(jsonPath("$[0].timeInMilliseconds", is(123456789)))
                .andExpect(jsonPath("$[0].fastestLapTime", is("lapTime1")))
                .andExpect(jsonPath("$[0].fastestLapSpeed", is("lapSpeed1")))
                .andExpect(jsonPath("$[0].status", is("status1")))
                .andExpect(jsonPath("$[0].ranking", is(1)))

                .andExpect(jsonPath("$[0].grandPrix.id", is(15)))
                .andExpect(jsonPath("$[0].grandPrix.year", is(2001)))
                .andExpect(jsonPath("$[0].grandPrix.round", is(6)))
                .andExpect(jsonPath("$[0].grandPrix.name", is("gpName111")))
                .andExpect(jsonPath("$[0].grandPrix.date", is("2001-01-01")))
                .andExpect(jsonPath("$[0].grandPrix.localization", is("localization111")))
                .andExpect(jsonPath("$[0].grandPrix.country", is("country111")))
                .andExpect(jsonPath("$[0].grandPrix.time", is("05:04:03")))
                .andExpect(jsonPath("$[0].grandPrix.url", is("url111")))
                .andExpect(jsonPath("$[0].grandPrix.driverName", is("driverName111")))
                .andExpect(jsonPath("$[0].grandPrix.raceResults").doesNotExist())
                .andExpect(jsonPath("$[0].grandPrix.qualificationResults").doesNotExist())
                .andExpect(jsonPath("$[0].grandPrix.circuit").doesNotExist())

                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].driverName", is("driverName2")))
                .andExpect(jsonPath("$[1].grandPrix.raceResults").doesNotExist())
                .andExpect(jsonPath("$[1].grandPrix.qualificationResults").doesNotExist())
                .andExpect(jsonPath("$[1].grandPrix.circuit").doesNotExist());
    }


    @Test
    void shouldReturnTeamsRaceResultsWithGrandPrixWithoutResultsAndCircuit() throws Exception {
        //given
        List<RaceResult> results = generateResults();
        when(raceResultService.getTeamResults(anyString(), anyInt())).thenReturn(results);

        //then
        mockMvc.perform(get("/api/race-result/team")
                .param("name", "name")
                .param("year", "2001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].driverId", is(11)))
                .andExpect(jsonPath("$[0].driverNumber", is("driverNumber1")))
                .andExpect(jsonPath("$[0].driverName", is("driverName1")))
                .andExpect(jsonPath("$[0].teamName", is("teamName1")))
                .andExpect(jsonPath("$[0].startingGridPosition", is(11)))
                .andExpect(jsonPath("$[0].finishingPosition", is("finishingPos1")))
                .andExpect(jsonPath("$[0].points", is(11.1)))
                .andExpect(jsonPath("$[0].laps", is("laps1")))
                .andExpect(jsonPath("$[0].time", is("time1")))
                .andExpect(jsonPath("$[0].timeInMilliseconds", is(123456789)))
                .andExpect(jsonPath("$[0].fastestLapTime", is("lapTime1")))
                .andExpect(jsonPath("$[0].fastestLapSpeed", is("lapSpeed1")))
                .andExpect(jsonPath("$[0].status", is("status1")))
                .andExpect(jsonPath("$[0].ranking", is(1)))

                .andExpect(jsonPath("$[0].grandPrix.id", is(15)))
                .andExpect(jsonPath("$[0].grandPrix.year", is(2001)))
                .andExpect(jsonPath("$[0].grandPrix.round", is(6)))
                .andExpect(jsonPath("$[0].grandPrix.name", is("gpName111")))
                .andExpect(jsonPath("$[0].grandPrix.date", is("2001-01-01")))
                .andExpect(jsonPath("$[0].grandPrix.localization", is("localization111")))
                .andExpect(jsonPath("$[0].grandPrix.country", is("country111")))
                .andExpect(jsonPath("$[0].grandPrix.time", is("05:04:03")))
                .andExpect(jsonPath("$[0].grandPrix.url", is("url111")))
                .andExpect(jsonPath("$[0].grandPrix.driverName", is("driverName111")))
                .andExpect(jsonPath("$[0].grandPrix.raceResults").doesNotExist())
                .andExpect(jsonPath("$[0].grandPrix.qualificationResults").doesNotExist())
                .andExpect(jsonPath("$[0].grandPrix.circuit").doesNotExist())

                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].driverName", is("driverName2")))
                .andExpect(jsonPath("$[1].grandPrix.raceResults").doesNotExist())
                .andExpect(jsonPath("$[1].grandPrix.qualificationResults").doesNotExist())
                .andExpect(jsonPath("$[1].grandPrix.circuit").doesNotExist());
    }

    private List<RaceResult> generateResults() {
        new ArrayList<>();
        List<RaceResult> raceResults = new ArrayList<>();
        RaceResult raceResult1 = new RaceResult();
        raceResult1.setId(1L);

        GrandPrix grandPrix1 = new GrandPrix();
        grandPrix1.setId(15L);
        grandPrix1.setYear(2001);
        grandPrix1.setRound(6);
        grandPrix1.setName("gpName111");
        grandPrix1.setDate(LocalDate.of(2001, 1, 1));
        grandPrix1.setLocalization("localization111");
        grandPrix1.setCountry("country111");
        grandPrix1.setTime(LocalTime.of(5, 4, 3));
        grandPrix1.setUrl("url111");
        grandPrix1.setDriverName("driverName111");

        grandPrix1.setRaceResult(Collections.singletonList(new RaceResult()));
        grandPrix1.setQualificationResult(Collections.singletonList(new QualificationResult()));
        raceResult1.setGrandPrix(grandPrix1);

        grandPrix1.setCircuit(new Circuit());

        raceResult1.setDriverId(11L);
        raceResult1.setDriverNumber("driverNumber1");
        raceResult1.setDriverName("driverName1");
        raceResult1.setTeamName("teamName1");
        raceResult1.setStartingGridPosition(11);
        raceResult1.setFinishingPosition("finishingPos1");
        raceResult1.setPoints(11.1f);
        raceResult1.setLaps("laps1");
        raceResult1.setTime("time1");
        raceResult1.setTimeInMilliseconds(123456789);
        raceResult1.setFastestLapTime("lapTime1");
        raceResult1.setFastestLapSpeed("lapSpeed1");
        raceResult1.setStatus("status1");
        raceResult1.setRanking(1);
        raceResults.add(raceResult1);

        RaceResult raceResult2 = new RaceResult();
        raceResult2.setId(2L);

        GrandPrix grandPrix2 = new GrandPrix();
        grandPrix2.setId(25L);
        grandPrix2.setRaceResult(Collections.singletonList(new RaceResult()));
        grandPrix2.setQualificationResult(Collections.singletonList(new QualificationResult()));
        grandPrix2.setCircuit(new Circuit());
        raceResult2.setGrandPrix(grandPrix2);

        raceResult2.setDriverId(22L);
        raceResult2.setDriverName("driverName2");
        raceResults.add(raceResult2);
        return raceResults;
    }
}

